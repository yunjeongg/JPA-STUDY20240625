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

import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("이름과 나이로 아이돌 조회하기")
    void searchTset() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        String name = "리즈";
        int age =20;

        //when - 테스트 상황
        Idol foundIdol = factory
                .select(idol)
                .from(idol)
                .where(idol.idolName.eq(name).and(idol.age.eq(age))) // 아이돌의 이름과 나이가 같은지
                .fetchOne();

        //then - 테스트 결과 단언
        assertNotNull(foundIdol);
        assertEquals("아이브", foundIdol.getGroup().getGroupName());

        System.out.println("\n\n\n\n");
        System.out.println("foundIdol = " + foundIdol);
        System.out.println("foundIdol.getGroup() = " + foundIdol.getGroup());
        System.out.println("\n\n\n\n");

        //        idol.idolName.eq("리즈") // idolName = '리즈'
//        idol.idolName.ne("리즈") // username != '리즈'
//        idol.idolName.eq("리즈").not() // username != '리즈'
//        idol.idolName.isNotNull() //이름이 is not null
//        idol.age.in(10, 20) // age in (10,20)
//        idol.age.notIn(10, 20) // age not in (10, 20)
//        idol.age.between(10,30) //between 10, 30
//        idol.age.goe(30) // age >= 30
//        idol.age.gt(30) // age > 30
//        idol.age.loe(30) // age <= 30
//        idol.age.lt(30) // age < 30
//        idol.idolName.like("_김%")  // like _김%
//        idol.idolName.contains("김") // like %김%
//        idol.idolName.startsWith("김") // like 김%
//        idol.idolName.endsWith("김") // like %김
    }

    @Test
    @DisplayName("조회 결과 반환하기")
    void fetchTest() {

        // 리스트 조회 (fetch)
        List<Idol> idolList = factory
                .select(idol)
                .from(idol)
                .fetch();

        System.out.println("\n\n=========== fetch =============");
        idolList.forEach(System.out::println);


        // 단일행 조회 (fetchOne)
        Idol foundIdol = factory
                .select(idol)
                .from(idol)
                .where(idol.age.lt(21))
                .fetchOne();

        System.out.println("\n\n=========== fetchOne =============");
        System.out.println("foundIdol = " + foundIdol);


        // 단일행 조회시 null safety를 위한 Optional로 받고 싶을 때
        // Optional<String> optionalName = Optional.ofNullable(name);
        // null 가능성이 있는 값을 Optional 객체로 감싸
        // 값이 null 이면 빈 Optional 객체를, null이 아니면 해당 값을 포함하는 Optional 객체를 반환한다.
        Optional<Idol> foundIdolOptional = Optional.ofNullable(factory
                .select(idol)
                .from(idol)
                .where(idol.age.lt(21))
                .fetchOne());

        Idol foundIdol2 = foundIdolOptional.orElseThrow();

        System.out.println("\n\n=========== fetchOne (Optional) =============");
        System.out.println("foundIdol2 = " + foundIdol2);
    }

    @Test
    @DisplayName("나이가 24세 이상인 아이돌 조회")
    void testAgeGoe() {
        // given
        int ageThreshold = 24;

        // when
        List<Idol> result = factory
                .selectFrom(idol) // selectFrom() - 해당 엔터티의 모든 컬럼을 조회할 때 사용
                .where(idol.age.goe(ageThreshold))
                .fetch();

        // then
        assertFalse(result.isEmpty());
        for (Idol idol : result) {
            System.out.println("\n\nIdol: " + idol);
            assertTrue(idol.getAge() >= ageThreshold);
        }
    }

    @Test
    @DisplayName("이름에 '김'이 포함된 아이돌 조회")
    void testNameContains() {
        // given
        String substring = "김";

        // when
        List<Idol> result = factory
                .selectFrom(idol)
                .where(idol.idolName.contains(substring))
                .fetch();

        // then
        assertFalse(result.isEmpty());
        for (Idol idol : result) {
            System.out.println("Idol: " + idol);
            assertTrue(idol.getIdolName().contains(substring));
        }
    }

    @Test
    @DisplayName("나이가 20세에서 25세 사이인 아이돌 조회")
    void testAgeBetween() {
        // given
        int ageStart = 20;
        int ageEnd = 25;

        // when
        List<Idol> result = factory
                .selectFrom(idol)
                .where(idol.age.between(ageStart, ageEnd))
                .fetch();

        // then
        assertFalse(result.isEmpty());
        for (Idol idol : result) {
            System.out.println("Idol: " + idol);
            assertTrue(idol.getAge() >= ageStart && idol.getAge() <= ageEnd);
        }
    }

    @Test
    @DisplayName("르세라핌 그룹에 속한 아이돌 조회하기")
    void testGroupEquals() {
        // given
        String groupName = "르세라핌";

        // when
        List<Idol> result = factory
                .selectFrom(idol)
                .where(idol.group.groupName.eq(groupName))
                .fetch();

        // then
        assertFalse(result.isEmpty());
        for (Idol idol : result) {
            System.out.println("Idol: " + idol);
            assertEquals(groupName, idol.getGroup().getGroupName());
        }
    }
}