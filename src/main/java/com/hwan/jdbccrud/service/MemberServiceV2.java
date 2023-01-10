package com.hwan.jdbccrud.service;


import com.hwan.jdbccrud.domain.Member;
import com.hwan.jdbccrud.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *  트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    /** 트랜잭션은 한 서비스안에서 시작하고 끝나야한다
     * 트랜잭션을 시작하려면 커넥션이 필요하다.
    */

    // 데이터소스를 가져온다다
   private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;


    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();

        try {
            // 수동 커밋 모드 시작작
           con.setAutoCommit(false); // 이렇게 해야 트랜잭션 시작.
            // 비즈니스 로직 시작
            Member fromMember = memberRepository.findById(fromId);
            Member toMember = memberRepository.findById(toId);

            memberRepository.update(fromId, fromMember.getMoney() - money);

            validation(toMember);
            memberRepository.update(toId, toMember.getMoney() + money);
            // 커밋, 롤백
            con.commit();
        } catch (Exception e) {
            con.rollback(); // 실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            // autoCommit을 false로 남아있으면 안된다
            // 때문에 autoCommit 모드를 다시 켜줘야해
           release(con);
        }


        }

    private void release(Connection con) {
        if (con != null) {
            try {

                con.setAutoCommit(true);
                con.close();
            } catch (Exception e) {
                log.info("error", "message", e);
            }
        }
    }

    private void validation(Member toMember) {
            if (toMember.getMemberId().equals("ex")) {
                throw new IllegalStateException("이체중 예외 발생");
            }
        }
    }


