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

    @Test
    @DisplayName("이름 오름차순 정렬 조회하기")
    void testSortByNameAsc() {
        // given

        // when
        List<Idol> sortedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.idolName.asc()) // .asc() 오름차순 생략시 오류나기때문에 꼭 써줘야한다.
                .fetch();

        // then
        assertFalse(sortedIdols.isEmpty());

        System.out.println("\n\n\n");
        sortedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 아이돌이 이름순으로 올바르게 정렬되었는지 확인
        assertEquals("가을", sortedIdols.get(0).getIdolName());
    }

    @Test
    @DisplayName("나이 내림차순 정렬 및 페이징 처리 조회하기")
    void testSortByAgeDescAndPaging() {
        // given
        int pageNumber = 0; // 첫 번째 페이지 (만약 클라이언트에서 0으로 시작이 아닌 1로 시작하는 페이지로 넘어오면 -1 따로 해줘야 한다.)
        int pageSize = 3; // 페이지당 데이터 수

        // when
        List<Idol> pagedIdols = factory
                .selectFrom(idol)
                .orderBy(idol.age.desc())
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();

        // then
        assertNotNull(pagedIdols);
        assertEquals(pageSize, pagedIdols.size());

        System.out.println("\n\n\n");
        pagedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 페이지의 첫 번째 아이돌이 나이가 가장 많은지 확인
        assertEquals("사쿠라", pagedIdols.get(0).getIdolName());
        assertEquals(26, pagedIdols.get(0).getAge());
    }

    @Test
    @DisplayName("특정 그룹의 아이돌을 이름 기준으로 오름차순 정렬 및 페이징 처리 조회")
    void testSortByNameAscAndPagingForGroup() {
        // given
        String groupName = "아이브";
        int pageNumber = 0; // 첫 번째 페이지
        int pageSize = 2; // 페이지당 데이터 수

        // when
        List<Idol> pagedIdols = factory
                .selectFrom(idol)
                .where(idol.group.groupName.eq(groupName))
                .orderBy(idol.idolName.asc())
                .offset(pageNumber * pageSize)
                .limit(pageSize)
                .fetch();

        // 이 방식으로 했을 때 log 에 아래같이 찍히는데,
        // cross 를 사용하면 모든 것을 조회하고, 그 후 .where 로 조건을 설정해서
        // 결과로 보면 문제가 없지만 성능에 좋지는 않다.
        // 자동으로 만들어 주는 방법의 결과가 맘에 드는지 꼭 log 를 살펴봐야 한다.
        // select
        //        idol0_.idol_id as idol_id1_3_,
        //        idol0_.age as age2_3_,
        //        idol0_.group_id as group_id4_3_,
        //        idol0_.idol_name as idol_nam3_3_
        //    from
        //        tbl_idol idol0_ cross
        //    join
        //        tbl_group group1_
        //    where
        //        idol0_.group_id=group1_.group_id
        //        and group1_.group_name=?
        //    order by
        //        idol0_.idol_name asc limit ?
        //2024-06-28 10:36:59.682  INFO 17516 --- [           main] p6spy                                    : #1719538619682 | took 5ms | statement | connection 3| url jdbc:mariadb://localhost/jpastudy?user=root&password=***
        //select idol0_.idol_id as idol_id1_3_, idol0_.age as age2_3_, idol0_.group_id as group_id4_3_, idol0_.idol_name as idol_nam3_3_ from tbl_idol idol0_ cross join tbl_group group1_ where idol0_.group_id=group1_.group_id and group1_.group_name=? order by idol0_.idol_name asc limit ?
        //select idol0_.idol_id as idol_id1_3_, idol0_.age as age2_3_, idol0_.group_id as group_id4_3_, idol0_.idol_name as idol_nam3_3_ from tbl_idol idol0_ cross join tbl_group group1_ where idol0_.group_id=group1_.group_id and group1_.group_name='아이브' order by idol0_.idol_name asc limit 2;

        // then
        assertNotNull(pagedIdols);
        assertEquals(pageSize, pagedIdols.size());

        System.out.println("\n\n\n");
        pagedIdols.forEach(System.out::println);
        System.out.println("\n\n\n");

        // 추가 검증 예시: 첫 번째 페이지의 첫 번째 아이돌이 이름순으로 올바르게 정렬되었는지 확인
        assertEquals("가을", pagedIdols.get(0).getIdolName());
    }
}