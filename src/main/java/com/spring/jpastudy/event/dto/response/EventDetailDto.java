package com.spring.jpastudy.event.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.jpastudy.event.entity.Event;
import lombok.*;

import java.time.LocalDate;

@Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetailDto {

    // 클라이언트에 줄 때 id 는 숫자타입에서 문자타입으로 변경해서 줘
    private String id;
    private String title;
    // 클라이언트에서 화면 랜더링 시 description 는 필요없어

    @JsonFormat(pattern = "yyyy년 mm월 dd일")
    private LocalDate startDate;

    @JsonProperty(value = "img-url")
    private String imgUrl;

    public EventDetailDto(Event event) {
        this.id = event.getId().toString();
        this.title = event.getTitle();
        this.startDate = event.getDate();
        this.imgUrl = event.getImage();
    }
}
