package com.spring.jpastudy.chap02.repository;

import com.spring.jpastudy.chap02.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// JPA 에서 테스트 할 때는 무조건 @Transactional, @Rollback 둘 다 붙이기
// 만약 기록하고 싶지 않다면 @Rollback(false) 만 추가하면 된다.
@Transactional
@Rollback(value = false)
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    void insertData() {
        Student s1 = Student.builder()
                .name("쿠로미")
                .city("청양군")
                .major("경제학")
                .build();

        Student s2 = Student.builder()
                .name("춘식이")
                .city("서울시")
                .major("컴퓨터공학")
                .build();

        Student s3 = Student.builder()
                .name("어피치")
                .city("제주도")
                .major("화학공학")
                .build();

        studentRepository.save(s1);
        studentRepository.save(s2);
        studentRepository.save(s3);
    }

    @Test
    @DisplayName("dummy test")
    void dummyTest() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("이름이 춘식이인 학생의 모든 정보를 조회한다.")
    void findByNameTest() {
        //given
        String name = "춘식이";

        //when
        List<Student> students = studentRepository.findByName(name);

        //then
        assertEquals(1, students.size());

        System.out.println("\n\n\n\n");
        System.out.println("students.get(0) = " + students.get(0));
        System.out.println("\n\n\n\n");
    }

    @Test
    @DisplayName("도시이름과 전공으로 학생을 조회")
    void findByCityAndMajorTest() {
        //given
        String city = "제주도";
        String major = "화학공학";
        //when
        List<Student> students = studentRepository.findByCityAndMajor(city, major);

        //then
        System.out.println("\n\n\n\n");
        System.out.println("students.get(0) = " + students.get(0));
        System.out.println("\n\n\n\n");
    }

    @Test
    @DisplayName("전공이 공학으로 끝나는 학생들 조회")
    void findByMajorContainingTest() {
        //given
        String majorContaining = "공학";
        //when
        List<Student> students = studentRepository.findByMajorContaining(majorContaining);
        //then
        System.out.println("\n\n\n");
        students.forEach(System.out::println);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("도시 또는 이름으로 학생을 조회")
    void nativeSQLTest() {
        //given
        String name = "춘식이";
        String city = "제주도";
        //when
        List<Student> students = studentRepository.getStudentByNameOrCity2(name, city);

        //then
        System.out.println("\n\n\n");
        students.forEach(System.out::println);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("JPQL로 학생 조회하기")
    void jpqlTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        String city = "제주도";

        //when - 테스트 상황
        Student student = studentRepository.getByCityWithJPQL(city)
                .orElseThrow(()-> new RuntimeException("조회된 학생이 없어요.")); // 학생이 조회가 안 될 경우 예외를 발생시키기

        //then - 테스트 결과 단언
        assertNotNull(student);
        // 에러 발생시키는 테스트를 할 때 에러가 나면 통과시키기
//        assertThrows(RuntimeException.class, ()-> new RuntimeException());

        System.out.println("\n\n\nstudent = " + student + "\n\n\n");
    }

    @Test
    @DisplayName("JPQL로 이름이 포함된 학생목록 조회하기")
    void jpqlTest2() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        String containingName = "춘";

        //when - 테스트 상황
        List<Student> students = studentRepository.searchByNameWithJPQL(containingName);

        //then - 테스트 결과 단언
        System.out.println("\n\n\n");
        students.forEach(System.out::println);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("JPQL로 삭제하기")
    void deleteJpqlTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        String name = "어피치";
        String city = "제주도";

        //when - 테스트 상황
        studentRepository.deleteByNameAndCityWithJPQL(name, city);

        //then - 테스트 결과 단언
        assertEquals(0, studentRepository.findByName(name).size());
    }
}