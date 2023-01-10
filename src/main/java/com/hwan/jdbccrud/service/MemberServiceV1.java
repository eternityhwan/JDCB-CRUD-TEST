package com.hwan.jdbccrud.service;


import com.hwan.jdbccrud.domain.Member;
import com.hwan.jdbccrud.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    /** 트랜잭션은 한 서비스안에서 시작하고 끝나야한다
     * 트랜잭션을 시작하려면 커넥션이 필요하다.
    */

    // repository 가져다 쓰기
    private final MemberRepositoryV2 memberRepository;

    // 보내는 애 받는애 금액 이렇게 세 개를 넘겨준다.
    // fromid 회원을 조회해서 toId 회원에게 money 만큼 돈을 전송하는 비즈니스 로직
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 맴버리포지토리에서 fromId를 꺼낸다
        Member fromMember = memberRepository.findById(fromId);
        // 맴버리포지토리에서 보낼 아이디를 꺼낸다.
        Member toMember = memberRepository.findById(toId);
        // fromId의 돈을 깍고
        // toId의 돈을 올린다.
        memberRepository.update(fromId, fromMember.getMoney() - money);
        // 검증에 문제가 없어야 다음 단계로 넘어간다.
        // 검증에서 문제가 생기면 두 번째 쿼리까지 넘어가지 않는다.
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);

        // 커밋, 롤백백
        }
        private void validation(Member toMember) {
            if (toMember.getMemberId().equals("ex")) {
                throw new IllegalStateException("이체중 예외 발생");
            }
        }
    }


