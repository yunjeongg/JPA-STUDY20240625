package com.spring.jpastudy.chap06_querydsl.service;

import com.spring.jpastudy.chap06_querydsl.entity.Idol;
import com.spring.jpastudy.chap06_querydsl.repository.IdolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
// MyBatis 에서 트랜잭션 관리를 위해 서비스계층에서 사용가능 (안써도 됨)
// JPA, QueryDSL 에서 트랜잭션 관리를 위해 서비스계층에서 사용가능
// (무조건 작성해줘야 한다, 쓸 때 붙여주지 않으면 프로그램이 돌아가지 않음 (예외가 터지면 자동으로 롤백해준다.))
@Transactional
public class IdolService {

    private final IdolRepository idolRepository;

    // 아이돌을 나이순으로 내림차 정렬해서 조회한다.
    public List<Idol> getIdols() {

        // 3번은 맞음, 1,2번 맞는지 확인해봐야 한다.

        // 1-1. 전체조회 한 후
        // List<Idol> idolList = idolRepository.findAll();
        // 1-2. stream 으로 정렬
        // return idolList.stream().sorted(Comparator.comparing(Idol::getAge)).collect(Collectors.toList());


        // 2-1.
        // List<Idol> idolList = idolRepository.findAllBySorted();

         // 2-2.
        // return idolList;


        // 3-1.
         List<Idol> idolList = idolRepository.findByGroupName();
        // 3-2.
         return idolList;
    }
}
