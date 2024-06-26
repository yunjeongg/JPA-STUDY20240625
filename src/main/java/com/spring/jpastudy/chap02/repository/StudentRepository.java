package com.spring.jpastudy.chap02.repository;

import com.spring.jpastudy.chap02.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {

    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    // https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html

    // 1. 쿼리 메서드 : 메서드의 이름에 특별한 규칙을 적용하면 SQL 이 규칙에 맞게 생성된다.
    // void findBy
    // void findByName (찾고 싶은 Entity 필드명을 카멜케이스로 작성)
    // void findByName (찾을 파라미터)
    // (찾을 대상이 단일인지, 복수인지) findByName(String name)
    List<Student> findByName(String name);

    // 2. 도시와 전공이 일치하는 학생 찾기
    // findBy (A And B)
    List<Student> findByCityAndMajor(String city, String major);

    // 3. 전공에 해당 키워드가 포함되는 학생 찾기
    // where major like '%major%'
    // findBy (전공Containing)
    List<Student> findByMajorContaining(String major);

    // 4. 전공이 해당 키워드로 시작되는 학생 찾기
    // where major like 'major%'
    List<Student> findByMajorStartingWith(String major);

    // 5. 전공이 해당 키워드로 끝나는 학생 찾기
    // where major like '%major'
    List<Student> findByMajorEndingWith(String major);

    // 6.
    // where age <=?
//    List<Student> findByAgeLessThanEqual(int age);





    // 7. native sql 사용하기 (순수하게 sql문 작성하기)
    // value - sql문 작성하고, ? 에는 (:임의의 이름) 으로 지어주기), nativeQuery = true)
    // @Query(value = "SELECT * FROM tbl_student WHERE stu_name= ? or city= ?, nativeQuery = true)
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name= :snm or city= :city", nativeQuery = true)
    // @Param 안에 위에서 지은 임의의이름 넣어주기
    List<Student> getStudentByNameOrCity(@Param("snm") String name, @Param("city") String city);

    // 8.
    @Query(value = "SELECT * " +
            "       FROM tbl_student " +
            "       WHERE stu_name = ?1 " +
            "           OR city = ?2", nativeQuery = true)
    List<Student> getStudentByNameOrCity2(String name, String city);


    /*
        - JPQL

        SELECT 엔터티별칭
        FROM 엔터티클래스명 AS 엔터티별칭
        WHERE 별칭.필드명

        ex) native - SELECT * FROM tbl_student WHERE stu_name = ?
            JPQL   - SELECT st FROM Student AS st WHERE st.name = ?
     */

    // 9.JPQL 사용하기 (순수하게 sql문 작성하기)
    // 도시명으로 학생 1명을 단일조회하기
    // 9-2.
    // @Query(value = "", nativeQuery = false) -- nativeQuery = false 는 생략 가능
    @Query(value = "SELECT st FROM Student st WHERE st.city=?1")
    // 9-1. Optional<> - null 이 발생할 수 있는 값을 감싸서 NPE(NullPointerException) 발생을 방지할 수 있다.
    Optional<Student> getByCityWithJPQL(String city);

    // 10. 특정 이름이 포함된 학생 리스트 조회하기
    @Query("SELECT stu FROM Student stu WHERE stu.name LIKE %?1%")
    List<Student> searchByNameWithJPQL(String name);

    // 11. JPQL 로 갱신 처리하기
    @Modifying // 11-1. JPQL 에서 SELECT 이외를 처리할 경우 무조건 붙여야 한다.
    @Query("DELETE FROM Student s WHERE s.name= ?1 AND s.city = ?2")
    void deleteByNameAndCityWithJPQL(String name, String city);
}