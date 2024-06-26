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

    // 양방향 연관관계에서 리스트에 데이터 갱신 시 주의사항
    @Test
    @DisplayName("양방향 연관관계에서 연관데이터 수정하기")
    void changeTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 3번 사원의 부서를 부서 2에서 부서 1로 수정한다.
        // 1. 3번사원 정보 조회
        Employee employee = employeeRepository.findById(3L).orElseThrow();

        // 1-1. 부서 1 정보 조회
        Department department = departmentRepository.findById(1L).orElseThrow();
        // 2-1. 부서 2 정보 조회
        Department department2 = departmentRepository.findById(2L).orElseThrow();

        //when - 테스트 상황
        // 1-2. 사원정보 수정하기 (기존 부서2 -> 부서 1로)
        // employee.setDepartment(department);
        // 2-2. 핵심 : 양방향에서는 수정시 반대편도 같이 수정해야 한다.
        // department.getEmployees().add(employee);
        // 3. 매번 두 쪽을 다 작성하는 것은 번거롭기 때문에 수정메소드를 따로 만들어둔다.
        employee.changeDepartment (department);

        // 1-3. 수정사항 저장하기
        // 2-3. 양방향 수정 저장하기
        employeeRepository.save(employee);

        //then - 테스트 결과 단언
        // 바뀐 부서의 사원목록 조회하기
        // 1-4. 사원정보가 Employee 엔터티에서 수정되어도 반대편 엔터티인 Department 에서는 리스트에 바로 반영되지 않는다.
        //      (조회시에 반영결과가 바로 보이지 않음)
        //      해결방안은 데이터 수정시에 반대편 엔터티에도 같이 수정해줘야 한다
        // 2-4. 양방향 수정 & 저장 결과 조회
        List<Employee> employees = department.getEmployees();

        System.out.println("\n\n\n\n");
        employees.forEach(System.out::println);
        System.out.println("\n\n\n\n");
    }
}