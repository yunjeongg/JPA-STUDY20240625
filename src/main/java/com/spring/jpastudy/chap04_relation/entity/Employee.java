package com.spring.jpastudy.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;

@Setter @Getter
@ToString
@EqualsAndHashCode(of = "id") // id만 비교
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_emp")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id; // 사원번호

    @Column(name = "emp_name", nullable = false)
    private String name; // 사원명

    // 1. Employee n : Department 1, 다대일관계
    @ManyToOne
    // 2. 단방향 매핑 - 데이터베이스처럼 한쪽에 상대방의 PK를 FK로 갖는 형태
    // 엔티티 간의 관계를 정의할 때 외래키 컬럼을 명시적으로 지정하기 위해 사용한다.
    @JoinColumn(name = "dept_id") // (name = "해당 필드(테이블) 의 pk의 @Column(name = "dept_name")
    private Department department; // 부서의 모든 정보를 갖고 있기
}
