//package com.courses.guidecourses.config;
//
//import com.courses.guidecourses.entity.CourseDocument;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ElasticsearchConfig {
//  private final ElasticsearchOperations operations;
//
//  @PostConstruct
//  public void createIndexAndMapping() {
//    var ops = operations.indexOps(CourseDocument.class);
//    if (ops.exists()) {
//      ops.delete();               // видаляємо старий індекс
//    }
//    ops.create();                  // створюємо новий
//    ops.putMapping(ops.createMapping());
//  }
//}
