package com.courses.guidecourses.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.courses.guidecourses.entity.CourseDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

  private final ElasticsearchOperations es;

  public List<CourseDocument> searchAll(String text) {
    // Створюємо універсальний Query із multi_match
    Query query = Query.of(q -> q
            .multiMatch(mm -> mm
                            .fields("title", "description", "directions", "topics")
                            .query(text)
                    // .type(MultiMatchQueryType.BEST_FIELDS) // за бажанням
                    // .minimumShouldMatch("75%")           // за бажанням
            )
    );

    NativeQuery nativeQuery = NativeQuery.builder()
            .withQuery(query)
            .build();

    SearchHits<CourseDocument> hits = es.search(nativeQuery, CourseDocument.class);
    return hits.stream()
            .map(SearchHit::getContent)
            .toList();
  }
}
