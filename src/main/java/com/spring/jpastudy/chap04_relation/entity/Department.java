package com.spring.jpastudy.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString(exclude = "employees") // 1-3. 사원정보를 조회하고 싶지 않은 경우 이와 같이 작성한다.
@EqualsAndHashCode(of = "id") // id만 비교
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_dept")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id; // 부서번호

    @Column(name = "dept_name", nullable = false)
    private String name; // 부서명

    // 1. Employee n : Department 1, 다대일관계
    /*
        양방향 매핑
        - 데이터베이스와 달리 객체지향 시스템에서 가능한 방법으로 1대 N관계에서, 1쪽에 N쪽의 데이터를 포함시킬 수 있는 방법이다.
        - 양방향 매핑에서 1쪽은 상대방 엔터티 갱신에 관여 할 수 없고
        (리스트에서 사원을 지운다고 실제 디비에서 사원이 삭제되지는 않는다는 말)
          단순히 읽기전용 (조회전용)으로만 사용하는 것이다.
     */
    // 1-1. @OneToMany mappedBy 에는 상대방 엔터티의 @ManyToOne에 대응되는 필드명을 꼭 적어야 한다.
    // 1-2. @OneToMany 의 fetch 기본값은 LAZY Loading
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY) // 상대방 엔터티에 이 엔터티가 저장된 필드명 적어주기

    // 2. @OneToMany 관계에서 상대방인 N쪽의 빈 컬렉션 객체를 초기생성하는 것이 좋다.
    // 이는 NPE(Null Pointer Exception)를 방지하고 코드의 안정성을 높이기 위해서이다.
    // 양방향 관계를 맺을 때 한 쪽에서 컬렉션이 비어있어도 다른쪽에서 이를 안전하게 다룰 수 있다.
    private List<Employee> employees = new ArrayList<>();
}
