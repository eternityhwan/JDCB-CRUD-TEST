package com.hwan.jdbccrud.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class UncheckedAppTest {

    @Test
    void unChecked() {

        CheckedAppTest.Controller controller = new CheckedAppTest.Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
            .isInstanceOf(Exception.class);
    }

    static class Controller {

        Service service = new Service();

        // 체크 예외는 계속 던저줘야한다
        public void request()  {
            service.logic();
        }

    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
           repository.call();
           networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }
            public void runSQL() throws SQLException {
                throw new SQLException("ex");
            }
        }

        // 런타임 연결 예외
    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        // 이 예외가 왜 터졌는이 예전 예외를 넣을 수 있다.
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}

