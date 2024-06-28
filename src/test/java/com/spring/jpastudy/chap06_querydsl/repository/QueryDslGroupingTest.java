package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
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

import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
// @Rollback(false)
class QueryDslGroupingTest {

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
        Group bts = new Group("방탄소년단");
        Group newjeans = new Group("뉴진스");

        groupRepository.save(leSserafim);
        groupRepository.save(ive);
        groupRepository.save(bts);
        groupRepository.save(newjeans);

        Idol idol1 = new Idol("김채원", 24, "여", leSserafim);
        Idol idol2 = new Idol("사쿠라", 26, "여", leSserafim);
        Idol idol3 = new Idol("가을", 22, "여", ive);
        Idol idol4 = new Idol("리즈", 20, "여", ive);
        Idol idol5 = new Idol("장원영", 20, "여", ive);
        Idol idol6 = new Idol("안유진", 21, "여", ive);
        Idol idol7 = new Idol("카즈하", 21, "여", leSserafim);
        Idol idol8 = new Idol("RM", 29, "남", bts);
        Idol idol9 = new Idol("정국", 26, "남", bts);
        Idol idol10 = new Idol("해린", 18, "여", newjeans);
        Idol idol11 = new Idol("혜인", 16, "여", newjeans);

        idolRepository.save(idol1);
        idolRepository.save(idol2);
        idolRepository.save(idol3);
        idolRepository.save(idol4);
        idolRepository.save(idol5);
        idolRepository.save(idol6);
        idolRepository.save(idol7);
        idolRepository.save(idol8);
        idolRepository.save(idol9);
        idolRepository.save(idol10);
        idolRepository.save(idol11);
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
    @DisplayName("성별별, 그룹별로 그룹화하여 아이돌의 숫자가 3명이하인 그룹만 조회한다.")
    void groupByGenderTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황
        // Tuple 는 JPA 에서 특정쿼리의 결과를 받기 위해 사용하는 인터페이스이다.
        // JPQL이나 Criteria API를 통해 생성할 수 있다.
        // 여러 필드를 갖는 복합적인 결과를 표현할 수 있다.
        // Tuple 를 사용하면 쿼리 결과를 엔터티 클래스가 아닌 객체로 받을 수 있다.

        // 1-1. 성별별로 그룹화하기
        // idol.gender 는 새로 추가한 내용이므로 Gradle 에서 build - clear, other - compileQuerydsl 해야 한다.
//        List<Tuple> idolList = factory.select(idol.gender, idol.count()).from(idol).groupBy(idol.gender).fetch();

        // 2-1. 성별별, 그룹별로 그룹화하기 (Tuple 객체로 쿼리결과 받기)
        List<Tuple> idolList = factory
                                    .select(idol.group, idol.gender, idol.count()) // 그룹별, 성별별, 총 인원수
                                    .from(idol)
                                    .groupBy(idol.gender, idol.group)
                                    .having(idol.count().loe(3)) // loe() 메소드는 주어진 값이 조건보다 같거나 작은 결과만 적용
                                    .fetch();
        /*
            위의 코드는 아래의 쿼리문과 같다.
            SELECT G.*, I.gender, COUNT(I.idol_id)
            FROM tbl_idol I
            JOIN tbl_group G
            ON I.group_id = G.group_id
            GROUP BY G.group_id, I.gender
         */

        //then - 테스트 결과 단언
        System.out.println("\n\n");
        // 1-1. 결과받기
        // System.out.println("idolList = " + idolList);

        // 2-2. 결과받기 Tuple 객체로 받은 값 추출하기
        // 추출한 값은 원래의 엔터티 타입, 필드타입으로 받을 수 있다.

        for (Tuple tuple : idolList) {

            Group group = tuple.get(idol.group);
            String gender = tuple.get(idol.gender);
            Long count = tuple.get(idol.count());

            System.out.println(
                    String.format("\n그룹명 : %s, 성별: %s, 인원수: %d\n"
                            , group.getGroupName(), gender, count)
            );
        }
        System.out.println("\n\n");
    }
}
