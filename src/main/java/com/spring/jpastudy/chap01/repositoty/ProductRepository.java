package com.spring.jpastudy.chap01.repositoty;

import com.spring.jpastudy.chap01.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// 1. extends JpaRepository 상속하기 (CRUD 기능 구현, 업데이트 제외)
// 2. <> 설정하기 (첫번째제너릭 - Entity 클래스명, 두번째제너릭 - 해당 Entity의 pk의 타입)
public interface ProductRepository extends JpaRepository<Product, Long> {

}
