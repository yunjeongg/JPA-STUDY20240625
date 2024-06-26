package com.spring.jpastudy.chap03_page;

import com.spring.jpastudy.chap02.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class StudentPageRepositoryTest {

    @Autowired
    StudentPageRepository repository;

    @BeforeEach
    void bulkInsert () {
        for (int i = 1; i <= 147; i++) {
            Student s = Student.builder().name("김시골" + i).city("도시" + i).major("숨쉬기" + i).build();

            repository.save(s);
        }
    }

    @Test
    @DisplayName("기본적인 페이지 조회 테스트")
    void basicPageTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        int pageNo = 6; // 1 페이지의
        int amount = 10; // 게시글 5개 조회

        // 1. 페이징 처리를 하기 위해서는 페이지 정보 객체(Pageable)를 생성해야 한다.
        // Pageable 은 인터페이스이기 때문에 객체생성이 안된다.
        // PageRequest.of 메소드는 PageRequest 객체를 생성하여 페이징과 정렬 정보를 설정한다.
        // 이 메소드는 여러 인자를 받을 수 있지만 가장 기본적인 형태로 페이지번호와 한 페이지에 포함될 데이터 개수를 받을 수 있다.
        // 2. 여기서 페이지번호는 zero-based 이다. (1페이지는 0으로 취급)
        // 1페이지는 0이기 때문에, 파라미터 (pageNo - 1)
        Pageable pageInfo = PageRequest.of(pageNo - 1, amount);

        //when - 테스트 상황
        Page<Student> students = repository.findAll(pageInfo);

        // 실질적인 데이터 꺼내기
        List<Student> studentList = students.getContent();

        // 총 페이지 수
        int totalPages = students.getTotalPages();

        // 총 학생 수
        long count = students.getTotalElements();

        //then - 테스트 결과 단언
        System.out.println("\n\n\n");
        System.out.println("totalPages = " + totalPages);
        System.out.println("count = " + count);
        System.out.println();
        studentList.forEach(System.out::println);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("페이징 + 정렬")
    void pagingAndSortTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 1. 하나의 조건으로 정렬하고 싶을 때
        // Pageable pageInfo = PageRequest.of(0, 10, Sort.by("name").descending()); // .descending() 내림차순
        // 1-1. PageRequest.of (1페이지, 10개, 게시글 매개값)
        // 게시글매개값으로는 엔터티의 필드명을 적어줘야 한다.
        // 1-2. Sort 의 기본값은 asc, .descending() 는 내림차순
        // 김시골 2, 김시골 10 비교시 앞부터 비교하기 때문에 10이 2보다 작다.
        // 김시골 2, 김시골 100 비교시 앞부터 비교하기 때문에 100이 2보다 작다.

        // 2. 여러 조건으로 정렬하고 싶을 때
        Pageable pageInfo = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("name"), Sort.Order.asc("city")));

        //when - 테스트 상황
        Page<Student> studentPage = repository.findAll(pageInfo);

        //then - 테스트 결과 단언
        System.out.println("\n\n\n");
        studentPage.getContent().forEach(System.out::println);
        System.out.println("\n\n\n");
    }
}