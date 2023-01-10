package com.hwan.jdbccrud.repository;

import com.hwan.jdbccrud.domain.Member;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.hwan.jdbccrud.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryTest {

    MemberRepositoryV2 repository;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager -- 항상 새로운 커넥션 획득
        // DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀 사용
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setPoolName("hwanPool");
        // 커넥션을 풀을 쓸 때는 커넥션 풀을 주입하면되고
        // Driver Manager를 쓸 때는 DriverManager를 쓰면된다.
        // 유연하게 선택 가능
        repository = new MemberRepositoryV2(dataSource);

    //     MemberRepository repository = new MemberRepository();
        }

    @Test
    void crud() throws SQLException {
        Member member = new Member("memberV100", 10000);
            repository.save(member);
            // SQLEception 이 올라오니까 처리해준다. throws SQLException

        //findById
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);

        // update: money 10000 -> 20000
        repository.update(member.getMemberId(), 20000);
        Member updatedMember = repository.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        repository.delete(member.getMemberId());
        assertThatThrownBy(() -> repository.findById(member.getMemberId()))
            .isInstanceOf(NoSuchElementException.class);

    }
}
