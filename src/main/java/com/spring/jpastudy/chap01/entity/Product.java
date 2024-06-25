package com.spring.jpastudy.chap01.entity;

import jdk.jfr.Category;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
// 7. @ToString 의 exclude
@ToString(exclude = "nickName") // 보여주는 것 중 nickName 제외하겠다.
// @ToString(exclude = {"nickName", "price"}) // 보여주는 것 중 여러가지 제외할 땐 {} 사용

@EqualsAndHashCode(of = "id") // 필드명 id 를 가지고 판단하겠다.
// @EqualsAndHashCode(of = {"id", "name") // id 와 name 이 같은 경우 같은객체
@NoArgsConstructor
@AllArgsConstructor
@Builder

// 1. Entity 로 사용할 클래스라는 것을 @Entity 를 사용해 지정 (기본 테이블 명 : 클래스명의 소문자)
@Entity
// 1-1. 테이블 명을 따로 지정하고 싶을 경우 @Table 사용
@Table(name = "tbl_product")
public class Product {

    // 2. 테이블의 컬럼명을 필드로 설정

    // 3. 테이블에서 id로 사용할 필드 설정
    @Id
    // 4. 컬럼 명을 따로 지정하고 싶을 경우 @Column 사용
    @Column(name = "prod_id")
    // 5. auto_increment 설정
    // MariaDB - IDENTITY, Oracle - SEQUENCE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    // 4-1. 기본값 prod_nm varchar(255) 으로 들어가고, 아래처럼 변경가능
    //      length = 30 (varchar(30)), nullable = false (not null)
    @Column(name = "prod_nm", length = 30, nullable = false)
    private String name; // 상품명

    @Column(name = "price")
    private int price; // 상품 가격

    // 4-2. 컬럼을 enum으로 설정할 경우 기본 타입 ORDINAL
    //      ORDINAL 은 저장시 enum의 인덱스
    //      STRING 은 저장시 enum의 문자열
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 상품 카테고리

    // 4-3. INSERT 시 자동으로 서버시간 저장
    @CreationTimestamp
    @Column(updatable = false) // 수정 불가
    private LocalDateTime createAt; // 상품 등록시간

    // 4-4. UPDATE 시 자동으로 수정시간 저장
    @UpdateTimestamp
    private LocalDateTime updatedAt; // 상품 수정시간

    // 4-5. DB 에는 저장안하고 클래스내부에서만 사용할 필드
    @Transient
    private String nickName;


    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }

    // 6. 컬럼에 값을 넣지 않았을 경우 기본값 설정
    @PrePersist
    public void prePersist () {
        if (this.price == 0) {
            this.price = 10000;
        }
        if (this.category == null) {
            this.category = Category.FOOD;
        }
    }

}
