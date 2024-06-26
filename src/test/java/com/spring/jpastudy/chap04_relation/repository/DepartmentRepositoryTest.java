package com.spring.jpastudy.chap04_relation.repository;

import com.spring.jpastudy.chap04_relation.entity.Department;
import com.spring.jpastudy.chap04_relation.entity.Employee;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    @DisplayName("특정 부서를 조회하면 해당 소속부서원들이 함께 조회된다.")
    void findDeptTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        Long id = 1L;

        //when - 테스트 상황
        Department department = departmentRepository.findById(id).orElseThrow();

        //then - 테스트 결과 단언
        System.out.println("\n\n\n\n");
        System.out.println("department = " + department);
        System.out.println("\n\n\n\n");

        // 1-4. 사원정보가 필요할 경우 그 때 추가하면 된다.
        List<Employee> employees = department.getEmployees();

        System.out.println("\n\n\n\n");
        employees.forEach(System.out::println);
        System.out.println("\n\n\n\n");
    }
}