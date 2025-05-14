package com.courses.guidecourses.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import com.courses.guidecourses.dto.CourseDto;
import com.courses.guidecourses.dto.SuggestionDto;
import com.courses.guidecourses.entity.Course;
import com.courses.guidecourses.entity.CourseDocument;
import com.courses.guidecourses.entity.Direction;
import com.courses.guidecourses.entity.Topic;
import com.courses.guidecourses.mapper.CourseDocumentMapper;
import com.courses.guidecourses.repository.CourseSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

  private final CourseSearchRepository repository;
  private final ElasticsearchOperations operations;
  private final ElasticsearchClient client;
  private final CourseDocumentMapper mapper;

  /** Індексувати або оновити документ */
  public void indexCourse(Course saved) {
    CourseDocument doc = mapper.toDocument(saved);
    doc.setTitleSuggest(new Completion(new String[]{ saved.getTitle() }));
    doc.setDirectionSuggest(new Completion(
            saved.getDirections().stream()
                    .map(Direction::getTitle)
                    .toArray(String[]::new)
    ));
    doc.setTopicSuggest(new Completion(
            saved.getTopics().stream()
                    .map(Topic::getTitle)
                    .toArray(String[]::new)
    ));
    operations.save(doc);
  }

  /** Видалити документ за ID */
  public void deleteCourse(Long courseId) {
    repository.deleteById(courseId);
  }

  /**
   * Пошук по фразі q у полях title, description, directions, topics
   */
  public Page<CourseDto> search(String q, Pageable pageable) {
    NativeQuery query = NativeQuery.builder()
            .withQuery(qb -> qb
                    .multiMatch(mm -> mm
                            .query(q)
                            .fields("title^3", "description", "directions", "topics")
                            .fuzziness("AUTO")
                    ))
            .withPageable(pageable)
            .build();

    SearchHits<CourseDocument> hits = operations.search(query, CourseDocument.class);
    List<CourseDto> dtos = hits.getSearchHits().stream()
            .map(SearchHit::getContent)
            .map(mapper::toDto)
            .toList();

    return new PageImpl<>(dtos, pageable, hits.getTotalHits());
  }

  public SuggestionDto suggest(String prefix){
      SearchResponse<CourseDocument> resp = null;
      try {
          resp = client.search(r -> r
                          .index("courses")
                          .suggest(s -> s
                                  .suggesters("byTitle",     sg -> sg.prefix(prefix)
                                          .completion(c -> c.field("titleSuggest")
                                                  .skipDuplicates(true)
                                                  .size(10)))
                                  .suggesters("byDirection", sg -> sg.prefix(prefix)
                                          .completion(c -> c.field("directionSuggest")
                                                  .skipDuplicates(true)
                                                  .size(10)))
                                  .suggesters("byTopic",     sg -> sg.prefix(prefix)
                                          .completion(c -> c.field("topicSuggest")
                                                  .skipDuplicates(true)
                                                  .size(10)))
                          ),
                  CourseDocument.class
          );
      } catch (IOException e) {
        throw new IllegalStateException("Failed to fetch suggestions from Elasticsearch", e);
      }

      Map<String, ? extends List<? extends Suggestion<?>>> suggestMap = resp.suggest();
    List<String> titles = extract(suggestMap, "byTitle");
    List<String> dirs   = extract(suggestMap, "byDirection");
    List<String> topics = extract(suggestMap, "byTopic");

    return new SuggestionDto(titles, dirs, topics);
  }

  private static List<String> extract(
          Map<String, ? extends List<? extends Suggestion<?>>> suggestions,
          String key
  ) {
    // витягаємо список; якщо ключа немає – порожній immutable-list
    List<? extends Suggestion<?>> list = suggestions.get(key);
    if (list == null) {
      list = List.of();          // або Collections.emptyList()
    }

    return list.stream()
            .filter(Suggestion::isCompletion)
            .flatMap(s -> s.completion().options().stream())
            .map(CompletionSuggestOption::text)
            .toList();
  }
}
