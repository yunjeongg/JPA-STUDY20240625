package com.spring.jpastudy.event.controller;

import com.spring.jpastudy.event.dto.request.EventSaveDto;
import com.spring.jpastudy.event.dto.response.EventDetailDto;
import com.spring.jpastudy.event.dto.response.EventOneDto;
import com.spring.jpastudy.event.entity.Event;
import com.spring.jpastudy.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class EventController {

    private final EventService eventService;

    // 전체 조회 요청
    @GetMapping
    public ResponseEntity<?> getList(
            @RequestParam(required = false, defaultValue = "date") String sort) {
        List<EventDetailDto> events = eventService.getEvents(sort);
        return ResponseEntity.ok().body(events);
    }

    // 등록 요청
    @PostMapping
    public ResponseEntity<?> register(@RequestBody EventSaveDto dto) {
        List<EventDetailDto> events = eventService.saveEvent(dto);
        return ResponseEntity.ok().body(events);
    }

    // 단일 조회 요청
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent (@PathVariable Long eventId) {

        if (eventId == null || eventId < 1) {

            String errorMessage = "eventId 가 정확하지 않습니다.";
            log.warn(errorMessage);

            return ResponseEntity.badRequest().body(errorMessage);
        }

        EventOneDto eventOne = eventService.getEventDetail(eventId);

        return ResponseEntity.ok().body(eventOne);
    }

    // PostMan
    // Get, http://localhost:8282/events/2 - 건강건강이벤트

    // 삭제요청
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> delete (@PathVariable Long eventId) {

        eventService.deleteEvent(eventId);

        return ResponseEntity.ok().body("event deleted");
    }

    // PostMan
    // Delete, http://localhost:8282/events/7 (url id 7번 게시글 삭제)

}
