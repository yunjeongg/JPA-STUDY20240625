package com.spring.jpastudy.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;

@Setter @Getter
//@ToString // 1-4-2. @ToString 주석처리하고 셀프로 ToString 구현하겠다.
@ToString(exclude = "department") // 1-6-2.연관관계 필드는 제외하고 조회하기
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
    // 1-1. EAGER Loading : 연관된 데이터를 항상 JOIN 을 통해 같이 가져온다.
    // @ManyToOne 의 fetch 기본값은 EAGER, 생략가능
    // --> 연관된 부서엔터티 무조건 같이 조회
    // 1-2. LAZY Loading : 해당 엔터티 데이터만 가져오고 필요한 경우 연관엔터티를 가져온다.
    // --> 필요한 경우에만 연관 연터티 같이 조회
    // 1-3. 이 경우 SELECT 가 두번 진행되는데(Employee 1번, Department 1번) 이는 @ToString 때문이다.
    // 1-4-1. 이를 방지하기 위해 toString() 메소드를 셀프로 만들어준다.

    // 1-5. 실무에서는 LAZY Loading 을 사용하는 것이 좋다.
    // 1-6-1. 만약 부서정보가 필요하다면 필요한 시점에 따로 조회하기
    @ManyToOne(fetch = FetchType.LAZY)
    // 2. 단방향 매핑 - 데이터베이스처럼 한쪽에 상대방의 PK를 FK로 갖는 형태
    // 엔티티 간의 관계를 정의할 때 외래키 컬럼을 명시적으로 지정하기 위해 사용한다.
    @JoinColumn(name = "dept_id") // (name = "해당 필드(테이블) 의 pk의 @Column(name = "dept_name")
    private Department department; // 부서의 모든 정보를 갖고 있기

//    // 1-4-3.
//    @Override
//    public String toString() {
//        return "Employee{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
////                ", department=" + department + // 필요없는 부서정보 주석처리하기
//                '}';
//    }
}
