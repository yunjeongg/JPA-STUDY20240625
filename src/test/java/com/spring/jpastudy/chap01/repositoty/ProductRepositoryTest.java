package com.spring.jpastudy.chap01.repositoty;

import com.spring.jpastudy.chap01.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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

    // 6. 테스트 생성
    @Test
    @DisplayName("상품을 데이터베이스에 저장한다.")
    void saveTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Product product = Product.builder()
                                            .name("정장")
                                            .price(120000)
                                            .category(Product.Category.FASHION)
                                            .build();

        //when - 테스트 상황
        // INSERT 후 저장된 데이터의 객체를 반환
        Product saved = productRepository.save(product);
        
        //then - 테스트 결과 단언
        assertNotNull(saved);
    }

}