package com.spring.jpastudy.chap03_page;

import com.spring.jpastudy.chap02.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // 이걸 가져와야 한다.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentPageRepository extends JpaRepository<Student, String> {
    
    // 1. 전체조회한 후 -> 페이징 처리하기
    // 몇 페이지, 몇 개의 게시글을 조회할지?
    Page<Student> findAll(Pageable pageable); // 기본적으로 구현되어 있어서 이건 안만들어도 된다.

    // 2. (이름)검색어를 검색한 후 + 페이징 처리하기
    // 2-1. Page<> -- ORDER BY & LIMIT 를 해 준다.
    Page<Student> findByNameContaining(String name, Pageable pageable);
}
