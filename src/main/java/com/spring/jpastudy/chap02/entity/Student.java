package com.spring.jpastudy.chap02.entity;

import lombok.*;
        import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
        import java.util.UUID;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_student")
public class Student {

    @Id
    @Column(name = "stu_id")
    // 1. 식별자 생성전략 선언하기
    // strategy -uuid (랜덤), increment (숫자 1씩증가), identity, sequence, native)
    // name - 식별자 생성전략의 이름
    @GenericGenerator(strategy = "uuid", name = "uid") // @GenericGenerator 의 name 을 작성
    // 2. DB Entity 의 기본값 생성전략 지정하기
    // generator - 1번 식별자 생성전략의 이름을 똑같이 써주기
    // strategy - auto, identity, sequence, table
    @GeneratedValue(generator = "uid")
    private String id;

    @Column(name = "stu_name", nullable = false)
    private String name;

    private String city;

    private String major;
}
