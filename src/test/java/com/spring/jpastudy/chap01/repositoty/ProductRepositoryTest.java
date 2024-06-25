package com.spring.jpastudy.chap01.repositoty;

import com.spring.jpastudy.chap01.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.spring.jpastudy.chap01.entity.Product.Category.*;
import com.spring.jpastudy.chap01.entity.Product;

import static org.junit.jupiter.api.Assertions.*;

// 1. @SpringBootTest 붙여주기
@SpringBootTest
// 2. 각각의 테스트메소드가 각각의 트랜잭션 내에서 실행되어 독립성을 갖고,
//    데이터 베이스의 일관성을 유지하며, 테스트 간 간섭을 방지할 수 있다.
@Transactional
// 3. 테스트가 종료된 후 DB 가 테스트 실행 전 상태로 유지됨
@Rollback
class ProductRepositoryTest {
    
    // 5. @Autowired 붙여주기
    @Autowired
    // 4. 테스트 할 클래스 필드로 생성
    ProductRepository productRepository;

    // 7.
    @BeforeEach
    void insertBeforeEach () {
        Product p1 = Product.builder()
                .name("아이폰")
                .category(ELECTRONIC)
                .price(2000000)
                .build();
        Product p2 = Product.builder()
                .name("탕수육")
                .category(FOOD)
                .price(20000)
                .build();
        Product p3 = Product.builder()
                .name("구두")
                .category(FASHION)
                .price(300000)
                .build();
        Product p4 = Product.builder()
                .name("주먹밥")
                .category(FOOD)
                .price(1500)
                .build();

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);
    }

    // 6. 테스트 생성
    @Test
    @DisplayName("상품을 데이터베이스에 저장한다.")
    void saveTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Product product = Product.builder()
                                            .name("떡볶이")
                                            .price(120000)
                                            .category(Product.Category.FASHION)
                                            .build();

        //when - 테스트 상황
        // INSERT 후 저장된 데이터의 객체를 반환
        Product saved = productRepository.save(product);
        
        //then - 테스트 결과 단언
        assertNotNull(saved);
    }

    // 8. 테스트 생성
    @Test
    @DisplayName("1번 상품을 삭제한다.")
    void deleteTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Long id = 1L;
        
        //when - 테스트 상황
        productRepository.deleteById(id);
        
        //then - 테스트 결과 단언
        // 만약 찾지 못할 경우 null을 반환한다.
        Product foundProduct = productRepository.findById(id)
                .orElse(null);

        assertNull(foundProduct);
    }

    // 9. 테스트 생성
    @Test
    @DisplayName("3번 상품을 단일조회하면 그 상품명이 구두이다")
    void findOneTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Long id = 3L;

        //when - 테스트 상황
        Product foundProduct = productRepository.findById(id).orElse(null);

        //then - 테스트 결과 단언
        assertEquals("구두", foundProduct.getName());
        System.out.println("\n\n\nfoundProduct = " + foundProduct + "\n\n\n");
    }

    // 10. 테스트 생성
    @Test
    @DisplayName("상품을 전체조회하면 상품의 총 개수가 4개이다.")
    void findAllTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황
        List<Product> productList = productRepository.findAll();

        //then - 테스트 결과 단언
        System.out.println("\n\n\n");

        productList.forEach(System.out::println);

        System.out.println("\n\n\n");

        assertEquals(4, productList.size());
    }

    // 11. 테스트 생성
    @Test
    @DisplayName("2번 상품의 이름과 카테고리를 수정한다")
    void modifyTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Long id = 2L;
        String newName = "청소기";
        Product.Category newCategory = ELECTRONIC;

        //when - 테스트 상황

        /*
            JPA 에서는 수정메소드를 따로 제공하지 않는다.
            - 단일 조회를 수행한 후 setter 을 통해 값을 변경한 후,
            - 다시 save 를 하면 INSERT 대신 UPDATE 문이 나간다.
         */
        Product product = productRepository.findById(id).orElse(null);
        product.setName(newName);
        product.setCategory(newCategory);

        Product saved = productRepository.save(product);

        //then - 테스트 결과 단언
        assertEquals(newName, saved.getName());
    }
}