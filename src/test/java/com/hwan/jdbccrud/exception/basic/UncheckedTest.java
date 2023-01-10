package com.hwan.jdbccrud.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
//        service.callThrow(); // <- 이렇게 돌리면 테스트 실패 뜬다.
        assertThatThrownBy(()-> service.callThrow())
            .isInstanceOf(HwanUncheckedException.class);
    }

    /**
     * RuntimeException을 상속받은 예외는 언체크 예외가 된다.
     */

    static class HwanUncheckedException extends RuntimeException {
        // 런타임 예외를 상속받은 클래스의 메세지를 받는 생성자 추가.
        public HwanUncheckedException(String message) {
            super(message);
        }
    }

    /** RuntimeException(UncheckedException) 예외를 상속 받으면
     안잡으면 밖으로 던진다는 선언을 안해줘도 된다.
     예외를 잡거나 던지지 않아도 된다는 것.
     예외를 잡지 않으면 자동으로 밖으로 던진다
     이게 체크 예외와 차이, 생략할 수 있다 Throws 체크예외
     */

    static class Service {
        Repository repository = new Repository();

        /**
         * 필요한 경우 예뢰르 잡아서 처리하면 된다.
         * 체크 예외든 언체크 예외든
         * 던지든 잡아서 처리를 하든 둘 중 하나.
         * 컴파일러가 체크를 하냐 안하느냐 차이
         */
        public void callCatch() {
            // 예외 처리가 안뜬다 가만 두면 그냥 throw 해버린다
            // 예외처리 할 수 있다.
            try {
                repository.call();
            } catch (HwanUncheckedException e) {
                log.info("예외처리, message");
            }
        }

        /**
         *  예외를 잡지 않아도 자연스럽게 상위로 넘어간다
         *  체크 예외와 다르게 throws 예외선언을 안해도된다.
         */

        public void callThrow() {
            repository.call();
        }


    }

    static class Repository {
        public void call() {
            throw new HwanUncheckedException("언체크익셉션");
        }
    }

}
