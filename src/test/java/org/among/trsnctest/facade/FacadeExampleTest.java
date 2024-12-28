package org.among.trsnctest.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FacadeExampleTest {
    /**
     * Spring 컨테이너가 아니라 JUnit5의 Jupiter가 의존성 주입을 담당하는데
     * @Autowired가 선언된 객체를 탐색하고 Spring 컨테이너에 요청하여 의존성을 주입
     */
    @Autowired
    private FacadeExample facadeExample;

    // DB 없는 환경에서의 정상 트랜잭션(커밋) 테스트
    @Test
    @Transactional  // 명시적 작성
    public void testWithNormalBehavior() {
        int result = facadeExample.testWithoutDb(3);
        assertEquals(12, result);
    }

    // DB 없는 환경에서의 예외 발생(롤백) 테스트
    @Test
    @Transactional
    public void testWithExceptionBehavior() {
        int result = 0; // 임의의 값
        try {
            result = facadeExample.testWithoutDb(11);
        } catch (RuntimeException e) { // 예외발생 시 롤백
            result = facadeExample.getStaticNumber();
        }

        assertEquals(1, result);
    }

    // DB 있는 환경에서의 예외 발생(롤백) 테스트
    // 롤백 되는 것 확인 위해 @Transactional 안 붙이고 try-catch문 사용했는데..
    // 이런 식으로 테스트하는 거 맞나 모르겠네..
    @Test
    public void testDbWithNormalBehavior() {
        try {
            facadeExample.testWithDb();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException :" + e);
        } finally {
            // N 업데이트 안 되는 것, 즉 롤백됨 확인
            assertEquals(facadeExample.selectStatus(1), "Y");
        }
    }
}
