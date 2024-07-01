package com.spring.jpastudy.chap06_querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_idol")
@Setter @Getter
@ToString(exclude = "group")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idol_id")
    private Long id;

    private String idolName;

    private int age;

    private String gender; // 성별 추가

    @ManyToOne(fetch = FetchType.LAZY) // LAZY 를 사용할 경우 따로 요청하지 않으면 해당 정보를 제공하지 않는다.
    @JoinColumn(name = "group_id")
    private Group group; // 그룹은 그룹의 모든 정보를 가지고 있지만


    public Idol(String idolName, int age, Group group) {
        this.idolName = idolName;
        this.age = age;
        if (group != null) {
            changeGroup(group);
        }
    }

    public Idol(String idolName, int age, String gender, Group group) {
        this.idolName = idolName;
        this.age = age;
        this.gender = gender;
        if (group != null) {
            changeGroup(group);
        }
    }


    public void changeGroup(Group group) {
        this.group = group;
        group.getIdols().add(this);
    }
}
