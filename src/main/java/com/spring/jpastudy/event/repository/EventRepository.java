package com.spring.jpastudy.event.repository;

import com.spring.jpastudy.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

// 1. 커스텀 레파지토리를 만들고, 원래 레포지토리에서 커스텀 레파지토리 상속받고
// 2. 커스텀 레파지토리의 구현체를 만들어야 한다. (EventRepositoryCustomImpl)
// 3. 구현체의 이름규칙은 원래레포지토리이름으로 시작 + 커스텀이름 + Impl
public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
}
