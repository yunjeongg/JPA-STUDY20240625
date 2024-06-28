package com.spring.jpastudy.chap06_querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap06_querydsl.entity.QIdol.idol;

// 1. 기존에 있는 IdolRepository 을 implements IdolRepository 상속받아와서 나만의 커스텀 메소드를 만들고자 할 때
// 문제 - IdolRepository 자체가 내장된 JpaRepository 을 상속받아온 상태이기 때문에
//       내장된 메소드들을 다 오버라이딩해와야 하고, 그 후에 나만의 커스텀메소드를 만들 수 있다.

// 해결방법 - 나만의 커스텀 메소드를 만들기 위해서 커스텀
// --> 나만의 커스텀 IdolCustomRepository IdolRepository 대신 커스텀 Repository 를 만들어 그 안에 나만의 커스텀 메소드를 만든 후
//     IdolRepository 대신 커스텀 Repository 을 implements IdolRepository 상속받기

// 주의사항 - IdolRepository 대신 커스텀 Repository 를 만들 때 기존의 IdolRepository 의 커스텀 Repository 로 인식하기 위해서
// 접두어로 JPA Repository 를 상속받은 인터페이스Repository (여기서는 IdolRepository),
// 접미어로 Impl 을 꼭 붙여줘야 한다.
// 아니면 프로그램이 커스텀 Repository 를 인식하지 못함.

@RequiredArgsConstructor
public class IdolRepositoryImpl implements IdolCustomRepository{

    // 1-1.
    private final JdbcTemplate template;

    @Override
    public List<Idol>  findAllSortedByName() {
        String sql = "SELECT * FROM tbl_idol ORDER BY idol_name ASC";
        return template.query(sql, (rs, n) -> {

            String idolName = rs.getString("idol_name");
            int age = rs.getInt("age");

            return new Idol(
                    idolName,
                    age,
                    null
            );
        });
    }

    // 2-1.
    private final JPAQueryFactory factory;

    @Override
    public List<Idol> findByGroupName() {
        return factory
                .select(idol)
                .from(idol)
                .orderBy(idol.group.groupName.asc())
                .fetch()
                ;
    }

    // 3-1
    @Override
    public Page<Idol> findAllByPaging(Pageable pageable) {

        // 페이징을 통한 조회
        List<Idol> idolList = factory.selectFrom(idol).orderBy(idol.age.desc()).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        // 총 조회건수
        Long totalCount = Optional.ofNullable(factory.select(idol.count()).from(idol).fetchOne()).orElse(0L);

        return new PageImpl<>(idolList, pageable, totalCount);
    }
}
