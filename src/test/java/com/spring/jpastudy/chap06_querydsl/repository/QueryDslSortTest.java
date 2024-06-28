package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
// @Rollback(false)
class QueryDslSortTest {

    @Autowired IdolRepository idolRepository;
    @Autowired GroupRepository groupRepository;

    // JPAQueryFactory 는 JPA 엔터티를 대상으로 복잡한 쿼리를 간결하고 안전하게 작성할 수 있도록 도와준다.
    @Autowired
    JPAQueryFactory factory;

    @BeforeEach
    void setUp () {
        //given
        Group leSserafim = new Group("르세라핌");
        Group ive = new Group("아이브");

        groupRepository.save(leSserafim);
        groupRepository.save(ive);

        Idol idol1 = new Idol("김채원", 24, leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, leSserafim);
        Idol idol3 = new Idol("가을", 22, ive);
        Idol idol4 = new Idol("리즈", 20, ive);
        Idol idol5 = new Idol("장원영", 20, ive);

        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
    }

    @Test
    @DisplayName("dummyTest")
    void dummyTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황

        //then - 테스트 결과 단언
    }

    @Test
    @DisplayName("QueryDSL 로 기본정렬하기")
    void sortingTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황
        List<Idol> sortedIdols = factory.selectFrom(idol).orderBy(idol.age.desc()).fetch();

        //then - 테스트 결과 단언
        assertFalse(sortedIdols.isEmpty()); // sortedIdols 가 비어있는게 거짓이라고 단언한다.

        System.out.println("\n\n\n");
        sortedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 아이돌이 나이가 가장 많고 이름이 올바르게 정렬되었는지 확인
        assertEquals("사쿠라", sortedIdols.get(0).getIdolName()); // 정렬의 첫번째 이름은 사쿠라라고 단언한다.
        assertEquals(26, sortedIdols.get(0).getAge()); // 정렬의 첫번째 나이는 26세라고 단언한다.
    }

    @Test
    @DisplayName("페이징 처리하기")
    void pagingTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 2-1.
        int pageNo = 1;
        int amount = 2;

        //when - 테스트 상황
        // 1-1.
//        List<Idol> pagedIdols = factory
//                                .selectFrom(idol)
//                                .orderBy(idol.age.desc())
//                                .offset(2).limit(2) // .offset(0).limit(2) -- 1페이지. 게시글 2개씩 표시
//                                // .offset(2).limit(2) // 2페이지. 게시글 2개씩 표시
//                                .fetch();

        // 2-2.
        List<Idol> pagedIdols = factory.selectFrom(idol).orderBy(idol.age.desc()).offset((pageNo - 1) * amount).limit(amount).fetch();

        // 2-3. 총 데이터 수
        // 총 아이돌의 개수를 세고, 만약 없을 경우
        Long totalCount = Optional.ofNullable(factory.select(idol.count()).from(idol).fetchOne()).orElse(0L);

        //then - 테스트 결과 단언
        // 1-2.
        // 2-3.
        System.out.println("\n\n\n");
        pagedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("Spring의 Page 인터페이스를 통한 페이징 처리")
    void pagingWithJpaTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Pageable pageInfo = PageRequest.of(0, 2);

        //when - 테스트 상황
        Page<Idol> pagedIdols = idolRepository.findAllByPaging(pageInfo);

        //then - 테스트 결과 단언
        assertNotNull(pagedIdols);
        assertEquals(2, pagedIdols.getSize());

        System.out.println("\n\n\n");
        pagedIdols.getContent().forEach(System.out::println);
        System.out.println("\n\n\n");
    }
}