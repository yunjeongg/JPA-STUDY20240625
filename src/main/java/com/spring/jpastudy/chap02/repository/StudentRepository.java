package com.spring.jpastudy.chap02.repository;

import com.spring.jpastudy.chap02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html

    // 1. 쿼리 메서드 : 메서드의 이름에 특별한 규칙을 적용하면 SQL 이 규칙에 맞게 생성된다.
    // void findBy
    // void findByName (찾고 싶은 Entity 필드명을 카멜케이스로 작성)
    // void findByName (찾을 파라미터)
    // (찾을 대상이 단일인지, 복수인지) findByName(String name)
    List<Student> findByName(String name);


}
