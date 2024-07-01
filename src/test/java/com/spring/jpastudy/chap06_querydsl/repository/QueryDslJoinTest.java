package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.dto.GroupAverageAgeDto;
import com.spring.jpastudy.chap06_querydsl.entity.Group;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.entity.QGroup;
import com.spring.jpastudy.chap06_querydsl.entity.QIdol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Transactional
// @Rollback(false)
class QueryDslJoinTest {

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
        Idol idol12 = new Idol("김종국", 48, "남", null); // 그룹 없음
        Idol idol13 = new Idol("아이유", 31, "여", null); // 그룹 없음


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
        idolRepository.save(idol12);
        idolRepository.save(idol13);

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
    @DisplayName("내부 조인 예제")
    void innerJoinTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황
        // 아이돌과 그룹의 정보
        // 1-1.
        List<Idol> idolList = factory.select(QIdol.idol).from(QIdol.idol).fetch();

        // 2-1-1.
//        List<Tuple> tupleList = factory.select(QIdol.idol, QGroup.group).from(QIdol.idol).fetch();
        // 2-2.
        List<Tuple> tupleList = factory
                .select(QIdol.idol, QGroup.group) // 아이돌 전부, 그룹의 전부 다 조회
                .from(QIdol.idol)
                .innerJoin(QIdol.idol.group, QGroup.group) // 2개를 넣어 on절 통합,
                // innerJoin 의 첫번째 파라미터는 from절에있는 엔터티의 연관객체,
                // innerJoin 의 두번째 파라미터는 실제로 조인할 엔터티
                .fetch();

        //then - 테스트 결과 단언
        // 1-2.
        for (Idol foundIdol : idolList) {
            System.out.println(foundIdol);
            System.out.println(foundIdol.getGroup());
        }

        // 2-1-2.   2-2-2.
        for (Tuple tuple : tupleList) {
            Idol tupleIdol = tuple.get(QIdol.idol);
            Group tupleGroup = tuple.get(QGroup.group);
            System.out.println(tupleIdol);
            System.out.println(tupleGroup);
        }

        // 2-1. 처럼 다른 테이블의 자료를 가져올 경우 프로그램이 어떤 join을 할지 모르기 때문에 2-2.처럼 명시해주는 게 좋다.
        // (이번 예제처럼 idol과 group 만 연관관계 있는 게 아니라 더 깊은 연관관계가 있을 수 있고, 연관관계가 더 존재할 수도 있기 때문에)
        // inner join 의 경우 보통 쿼리문을 작성할 경우 innerJoin~ on~ 이런식으로 작성해줘야 하지만,
        // 여기서는 inner join 에 파라미터 두 개만 넣어주면 된다.
    }

    @Test
    @DisplayName("Left Outer Join")
    void outerJoinTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황
        List<Tuple> result = factory.select(idol, QGroup.group).from(idol).leftJoin(idol.group, QGroup.group).fetch();

        //then - 테스트 결과 단언
        assertFalse(result.isEmpty());
        for (Tuple tuple : result) {
            Idol i = tuple.get(idol);
            Group g = tuple.get(QGroup.group);

            // 그룹이 있으면 그룹 가져오고, 없으면 솔로가수라고 작성하기
            System.out.println("\nIdol: " + i.getIdolName() + ", Group: " + (g != null ? g.getGroupName() : "솔로가수"));

            // 만약 NVL 같은 조건함수를 무조건 써야 하는 경우라면 native join 을 사용해야 하고, IdolRepositoryImpl 의 2-1 사용.
        }
    }
}
