package com.hwan.jdbccrud.service;

import com.hwan.jdbccrud.connection.ConnectionConst;
import com.hwan.jdbccrud.domain.Member;
import com.hwan.jdbccrud.repository.MemberRepositoryV2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.hwan.jdbccrud.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/***
 기본동작, 트랜잭셔 없어 문제 발생 하는 테스트
*/

class MemberServiceV1Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";
    private static final String MEMBER_EX = "ex";

    private MemberRepositoryV2 memberRepository;
    private MemberServiceV1 memberService;

    @BeforeEach // 테스트 작동전 동작시키는 것 설정 파일들을 기동시킬 때 쓰자
    void before() {
        // 리포지토리가 데이터 소스가 있어야하니까 드라이버 매니저 쓴다.
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV2(dataSource);
        memberService = new MemberServiceV1(memberRepository);
        }

        // 테스트 후 작동 메서드드
       @AfterEach
        void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
        }

        //정상 이체 로직
        @Test
        @DisplayName("정상 이체")
        void accountTransfer() throws SQLException {

            // 뭐가 주어졌는가
            Member memberA = new Member(MEMBER_A, 10000);
            Member memberB = new Member(MEMBER_B, 10000);
            memberRepository.save(memberA);
            memberRepository.save(memberB);
            // 언제 사용되는가
            memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 20000);

            // then
            Member findMemberA = memberRepository.findById(memberA.getMemberId());
            Member findMemberB = memberRepository.findById(memberB.getMemberId());

            assertThat(findMemberA.getMoney()).isEqualTo(10000);
            assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }
}
