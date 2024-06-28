package com.spring.jpastudy.chap06_querydsl.repository;

import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 내장된 JpaRepository 뿐 아니라 내가 만든 커스텀Repository 까지 상속하면 (다중상속)
// 프로그램이 두 Repository 의 내장메소드를 모두 인식할 수 있다.
public interface IdolRepository extends JpaRepository<Idol, Long>, IdolCustomRepository {

    @Query("SELECT i FROM Idol i ORDER BY i.age DESC")
    List<Idol> findAllBySorted ();
}
