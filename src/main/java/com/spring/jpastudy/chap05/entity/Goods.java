package com.spring.jpastudy.chap05.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString(exclude = "purchaseList")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_mtm_goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Long id;

    @Column(name = "goods_name")
    private String name;

    @OneToMany(mappedBy = "goods", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Purchase> purchaseList = new ArrayList<>();

    // @ManyToMany
    // 연관관계 설정시 사용하지 않는 걸 권장한다.
    // 연관관계 주인설정도 힘들고, 중간테이블을 자기맘대로 만들어버린다.
    //                                 1  -    N   -  1
    // 차라리 Entity 3개를 만드는게 좋다. 상품 - 구매이력 - 유저
    //                                    -구매이력 -
}
