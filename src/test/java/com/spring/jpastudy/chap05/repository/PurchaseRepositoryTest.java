package com.spring.jpastudy.chap05.repository;

import com.spring.jpastudy.chap05.entity.Goods;
import com.spring.jpastudy.chap05.entity.Purchase;
import com.spring.jpastudy.chap05.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class PurchaseRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    EntityManager em; // 2-1. 영속성 컨텍스트(persistence context)를 관리하는 객체

    private User user1;
    private User user2;
    private User user3;
    private Goods goods1;
    private Goods goods2;
    private Goods goods3;

    @BeforeEach
    void setUp() {
        user1 = User.builder().name("망곰이").build();
        user2 = User.builder().name("하츄핑").build();
        user3 = User.builder().name("쿠로미").build();
        goods1 = Goods.builder().name("뚜비모자").build();
        goods2 = Goods.builder().name("닭갈비").build();
        goods3 = Goods.builder().name("중식도").build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        goodsRepository.save(goods1);
        goodsRepository.save(goods2);
        goodsRepository.save(goods3);
    }

    @Test
    @DisplayName("dummyTest")
    void dummyTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터

        //when - 테스트 상황

        //then - 테스트 결과 단언
    }


    @Test
    @DisplayName("유저와 상품을 연결한 구매 기록 생성 테스트")
    void createPurchaseTest() {
        //given
        //given - 테스트에 주어질 데이터
        Purchase purchase = Purchase.builder()
                .user(user2)
                .goods(goods1)
                .build();
        //when

        purchaseRepository.save(purchase);

        // 2-2. 영속성 컨텍스트를 초기화하면 SELECT문을 볼 수 있다. (실무에서는 초기화하지 않는것이 성능최적화에 도움이 된다.)
        em.flush();
        em.clear();

        //then
        Purchase foundPurchase = purchaseRepository.findById(purchase.getId()).orElseThrow();

        System.out.println("\n\n\n구매한 회원정보: " + foundPurchase.getUser() + "\n\n");
        System.out.println("\n\n\n구매한 상품정보: " + foundPurchase.getGoods() + "\n\n");

        assertEquals(user2.getId(), foundPurchase.getUser().getId()); // 단언 : 유저2과 구매이력의 아이디는 같을것이다.
        assertEquals(goods1.getId(), foundPurchase.getGoods().getId()); // 단언 : 유저2과 구매이력의 아이디는 같을것이다.
    }

    @Test
    @DisplayName("특정 유저의 구매목록을 조회한다.")
    void findPurchaseListTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 1-1. 구매기록 생성 1
        Purchase purchase1 = Purchase.builder().user(user1).goods(goods1).build();
        // 1-2. 구매기록 생성 2
        Purchase purchase2 = Purchase.builder().user(user1).goods(goods3).build();

        //when - 테스트 상황
        // 1-3.
        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);

        // 1-4.
        em.flush();
        em.clear();

        //then - 테스트 결과 단언
        //1-5.
        User user = userRepository.findById(user1.getId()).orElseThrow(); // 회원 1
        List<Purchase> purchases = user.getPurchaseList(); // 회원 1의 구매목록

        for (Purchase p : purchases) {
            System.out.printf("\n\n%s 님이 구매한 물품명 : %s\n\n",
                    user.getName(), p.getGoods().getName());
        }

        assertEquals(2, purchases.size());
        assertTrue(purchases.stream().anyMatch(p -> p.getGoods().equals(goods1))); // goods1 과 일치하는 구매목록이 한개이상 존재할것이다
        assertTrue(purchases.stream().anyMatch(p -> p.getGoods().equals(goods3))); // goods2 과 일치하는 구매목록이 한개이상 존재할것이다
    }

    @Test
    @DisplayName("특정 상품을 구매한 유저목록을 조회한다.")
    void findUserByGoodsTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 1-1. 구매기록 생성 1
        Purchase purchase1 = Purchase.builder().user(user2).goods(goods1).build();
        // 1-2. 구매기록 생성 2
        Purchase purchase2 = Purchase.builder().user(user3).goods(goods1).build();

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);

        em.flush();
        em.clear();

        //when - 테스트 상황
        Goods goods = goodsRepository.findById(goods1.getId()).orElseThrow();
        List<Purchase> purchases = goods.getPurchaseList();

        //then - 테스트 결과 단언
        for (Purchase p : purchases) {
            System.out.printf("\n\n%s 상품을 구매한 유저명 : %s\n\n",
                    goods.getName(), p.getUser().getName());
        }

        assertEquals(2, purchases.size());
        assertTrue(purchases.stream().anyMatch(p -> p.getUser().equals(user2))); // user2 와 일치하는 구매목록이 한개이상 존재할것이다
        assertTrue(purchases.stream().anyMatch(p -> p.getUser().equals(user3))); // user3 와 일치하는 구매목록이 한개이상 존재할것이다
    }

    @Test
    @DisplayName("구매기록 삭제 테스트")
    void deletePurchaseTest() {
        // gwt 패턴
        //given - 테스트에 주어질 데이터
        // 1-1. 구매기록 생성 1
        Purchase purchase = Purchase.builder().user(user1).goods(goods1).build();

        Purchase savedPurchase = purchaseRepository.save(purchase);

        em.flush();
        em.clear();

        //when - 테스트 상황
        purchaseRepository.delete(savedPurchase);

        em.flush();
        em.clear();

        //then - 테스트 결과 단언
        Purchase foundPurchase = purchaseRepository.findById(purchase.getId()).orElse(null); // 못찾을 경우 null 반환

        assertNull(foundPurchase);
    }

    @Test
    @DisplayName("회원이 탈퇴하면 구매기록이 모두 삭제되어야 한다")
    void cascadeRemoveTest() {
        //given
        Purchase purchase1 = Purchase.builder()
                .user(user1).goods(goods2).build(); // 유저 1이 good2 구매 생성

        Purchase purchase2 = Purchase.builder()
                .user(user1).goods(goods3).build(); // 유저 1이 good3 구매 생성

        Purchase purchase3 = Purchase.builder()
                .user(user2).goods(goods1).build(); // 유저 2이 good1 구매 생성

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);
        purchaseRepository.save(purchase3);

        em.flush();
        em.clear();

        User user = userRepository.findById(user1.getId()).orElseThrow();
        List<Purchase> purchases = user.getPurchaseList();

        System.out.println("\n\nuser1's purchases = " + purchases + "\n\n");
        System.out.println("\n\nall of purchases = " + purchaseRepository.findAll() + "\n\n"); // 총 구매기록 (3개)

        userRepository.delete(user); // 회원탈퇴 (유저1) -- 먼저 구매이력 삭제 -> 회원삭제

        em.flush();
        em.clear();

        //when

        List<Purchase> purchaseList = purchaseRepository.findAll(); // 구매기록 전체조회 (1개)

        System.out.println("\n\nafter removing purchaseList = " + purchaseList + "\n\n");

        //then
        assertEquals(1, purchaseList.size()); // 구매기록을 전체조회하면 1개만 남아있을 것이다.
    }
}
