package com.hwan.jdbccrud.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.net.URL;
import static com.hwan.jdbccrud.connection.ConnectionConst.URL;
import static com.hwan.jdbccrud.connection.ConnectionConst.PASSWORD;
import static com.hwan.jdbccrud.connection.ConnectionConst.USERNAME;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV2Test {

    MemberRepositoryV2 memberRepositoryV2;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager -- 항상 새로운 커넥션 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        memberRepositoryV2 = new MemberRepositoryV2(dataSource);
    }

}
