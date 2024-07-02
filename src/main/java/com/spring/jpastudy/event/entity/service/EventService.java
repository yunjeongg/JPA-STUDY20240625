package com.spring.jpastudy.event.entity.service;

import com.spring.jpastudy.event.dto.request.EventSaveDto;
import com.spring.jpastudy.event.entity.Event;
import com.spring.jpastudy.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional // JPA 에서는 Service 클래스에 무조건 붙여야함 (오류나면 자동적으로 롤백해줌)
public class EventService {

    private final EventRepository eventRepository;

    // 전체 조회 서비스
    public List<Event> getEvents (String sort) {
        return eventRepository.findEvents(sort);
    }

    // 이벤트 등록
    public List<Event> saveEvent (EventSaveDto dto) {
        Event savedEvent = eventRepository.save(dto.toEntity());
        log.info("saved event: {}", savedEvent);

        return getEvents("date"); // 정렬방식 - 날짜정렬 
    }
}
