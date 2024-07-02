package com.spring.jpastudy.event.controller;

import com.spring.jpastudy.event.dto.request.EventSaveDto;
import com.spring.jpastudy.event.entity.Event;
import com.spring.jpastudy.event.entity.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 리액트와 연결된 컨트롤러는 비동기 RestController 를 사용할 것이다.
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class EventController {

    private final EventService eventService;

    // 전체조회 요청
    @GetMapping
    public ResponseEntity<?> getList (String sort) {
        List<Event> events = eventService.getEvents(sort);

        return ResponseEntity.ok().body(events);
    }

    // 등록 요청
    @PostMapping
    public ResponseEntity<?> register(@RequestBody EventSaveDto dto) {
        List<Event> events = eventService.saveEvent(dto);
        return ResponseEntity.ok().body(events);
    }

}
