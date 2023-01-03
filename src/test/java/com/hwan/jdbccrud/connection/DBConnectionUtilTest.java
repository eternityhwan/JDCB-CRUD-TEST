package com.hwan.jdbccrud.connection;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;


public class DBConnectionUtilTest {

    @Test
    void Connection() {
        Connection connection = DBConnectionUtil.getConnection();
        Assertions.assertThat(connection).isNotNull();
    }


}
