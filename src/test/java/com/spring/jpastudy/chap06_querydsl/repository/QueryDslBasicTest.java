package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.entity.QIdol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
// @Rollback(false)
class QueryDslBasicTest {

    @Autowired IdolRepository idolRepository;
    @Autowired GroupRepository groupRepository;

    @Autowired
    EntityManager em;

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

        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
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
    @DisplayName("JPQL 로 특정이름의 아이돌 조회하기")
    void jpqlTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        String jpqlQuery = "SELECT i FROM Idol i WHERE i.idolName = ?1";

        //when - 테스트 상황
        Idol foundIdol = em.createQuery(jpqlQuery, Idol.class)
                .setParameter(1, "가을") // jpqlQuery 파라미터 ? 채우기
                .getSingleResult();

        //then - 테스트 결과 단언
        assertEquals("아이브", foundIdol.getGroup().getGroupName());

        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + foundIdol);
        System.out.println("foundIdol.getGroup() = " + foundIdol.getGroup());
        System.out.println("\n\n\n\n");
    }

    @Test
    @DisplayName("QueryDsl 로 특정 이름의 아이돌 조회하기")
    void queryDslTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 1-1. QueryDSL 로 JPQL을 만드는 빌더
//        JPAQueryFactory factory = new JPAQueryFactory(em);

        //when - 테스트 상황
        // factory.select().from().join().on().where().groupBy().having().orderBy().offset().limit(); // QueryDsl 양식
        //when
//        Idol foundIdol = factory // ()안은 Q타입으로 작성하기, ctrl + 클릭으로 들어가서 찾기
//                .select(QIdol.idol)
//                .from(QIdol.idol)
//                .where(QIdol.idol.idolName.eq("사쿠라"))
//                .fetchOne(); // 단일조회시

        Idol foundIdol = factory // ()안은 Q타입으로 작성하기, ctrl + 클릭으로 들어가서 찾기
                .select(idol)
                .from(idol)
                .where(idol.idolName.eq("사쿠라"))
                .fetchOne(); // 단일조회시

        //then - 테스트 결과 단언
        assertEquals("르세라핌", foundIdol.getGroup().getGroupName());

        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + foundIdol);
        System.out.println("foundIdol.getGroup() = " + foundIdol.getGroup());
        System.out.println("\n\n\n\n");
    }
}