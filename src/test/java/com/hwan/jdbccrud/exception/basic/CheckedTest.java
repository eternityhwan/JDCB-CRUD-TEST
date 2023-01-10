package com.hwan.jdbccrud.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CheckedTest {

    // catch 성공 테스트
    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }
    // throw 성공 테스트
    // 예외를 던진 것이다.
    @Test
    public void checked_throw()  {
        Service service = new Service();
//        service.callThrow(); // 이대로 throw 던지고 실행시키면 테스트 실패
        assertThatThrownBy(() -> service.callThrow())
            .isInstanceOf(HwanCheckedException.class);
    }

    /**
     *
     * Exception을 상속받은 예외는 체크 예외가 된다.
     */

    static class HwanCheckedException extends Exception {
        // 메세지 받는 생성자 추가
        public HwanCheckedException(String message) {
            super(message);
            // super <- 부모에서 메세지를 가져온다.
        }
    }

    static class Service {
        Repository repository = new Repository();

        /**
         * Checked 예외는 예외를 잡아서 처리하거나,
         * 던지거나 둘 중 하나를 필수로 해야한다.
         * 예외를 잡아서 처리하는 코드
         */

        public void callCatch() {
            // 얘도 잡거나 던져야 하는데 여기서는 잡을 것이다
            try {
                repository.call();
            } catch (HwanCheckedException e) {
                // 예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws 예외를
         * 메서드에 필수로 선언해야한다 (컴파일에러난다 안해주면)
         * */
        public void callThrow() throws HwanCheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws HwanCheckedException {
            // 예외는 잡거나 던저야한다 -- 잡지 않았으니까 던져야지
            // throws HwanCheckedException 추가해줘야한다.
            // 체크 예외는 잡거나 던져줘야 해.
            throw new HwanCheckedException("ex");
        }

    }
}
