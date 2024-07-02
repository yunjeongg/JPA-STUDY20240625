package com.spring.jpastudy.event.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.jpastudy.event.entity.Event;
import com.spring.jpastudy.event.entity.QEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.jpastudy.event.entity.QEvent.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<Event> findEvents(String sort) {
        return factory
                .selectFrom(event)
                .orderBy(specifier(sort))
                .fetch()
                ;
    }

    // 정렬 조건을 처리하는 메서드
    private OrderSpecifier<?> specifier(String sort) {
        switch (sort) {
            case "date":
                return event.date.desc();
            case "title":
                return event.title.asc();
            default:
                return null;
        }
    }

    // http://localhost:8282/events

    // {
    //    "title": "dummy event",
    //    "desc": "welcome 재밌는 이벤트",
    //    "imageUrl": "https://mblogthumb-phinf.pstatic.net/MjAyMzEwMTdfNzUg/MDAxNjk3NTMyODE5OTY4.xiO7cB-xcTyXisExBNH-Fe6FUO_DRddYaoJSCpxrh5Eg.f9tXeVeDvCG_W-3op6D2tieH7DGkPg7e81HN4wInKuEg.JPEG.kintex_korea/231017-%EC%9B%94%EA%B0%84-%ED%82%A8%ED%85%8D%EC%8A%A4%ED%94%BC%EB%A6%AC%EC%96%B8%EC%8A%A4-1.jpg?type=w800",
    //    "beginDate": "2024-01-30"
    // }

    // 아래 정확하지 않음
    // http://localhost:8282/events?sort=date
}

