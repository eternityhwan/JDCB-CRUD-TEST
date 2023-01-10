package com.hwan.jdbccrud.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class CheckedAppTest {

    @Test
    void checked() {

        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
            .isInstanceOf(Exception.class);
    }

    static class Controller {

        Service service = new Service();

        // 체크 예외는 계속 던저줘야한다
        public void request() throws SQLException, ConnectException {
            service.logic();
        }

    }


    /**
     * throws SQLException, ConnectException 처럼
     * 예외를 던지는 부분을 코드에 선언하는 것이 왜 문제가 될까?
     * 바로 서비스, 컨트롤러에서 java.sql.SQLException 을 의존하기 때문에 문제가 된다.
     * 향후 리포지토리를 JDBC 기술이 아닌 다른 기술로 변경한다면, 그래서 SQLException 이 아니라 예를
     * 들어서 JPAException 으로 예외가 변경된다면 어떻게 될까?
     * SQLException 에 의존하던 모든 서비스, 컨트롤러의 코드를 JPAException 에 의존하도록 고쳐야 한다.
     */
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException {
            repository.call(); // 두 메서드 다 던져야함
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        // 체크 익셉션이니까 예외처리 해준다
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
