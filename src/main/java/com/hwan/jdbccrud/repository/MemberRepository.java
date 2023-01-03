package com.hwan.jdbccrud.repository;


import com.hwan.jdbccrud.connection.DBConnectionUtil;
import com.hwan.jdbccrud.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

//**
// JDBC - DriverManager 사용해서 저장.
//
// */
@Slf4j
public class MemberRepository {

    // Create
    public Member save(Member member) throws SQLException {
        //insert 쿼리
        String sql = "insert into member(member_id, money) values (?, ?)";
        // 커넥션이 있어야 연결을 하지
        Connection con = null;
        // preparestatement로 쿼리를 날린다.
        PreparedStatement pstmt = null;

        con = getConnection();
        try {
            pstmt = con.prepareStatement(sql);
            // 아래처럼 해주면 상단에 sql에 ?,?에 아래 값이 들어간다.
            // 파라미터 바인딩을 한다.
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); // int를 반환하는데 영향 받은 DB row 수를 반환한다.
            return member;
        } catch (SQLException e) {
            // 예외가 나오면 로그를 남김
            log.error("db error", e);
            throw e;
        } finally {
            // 프리페어 스테이츠먼츠와 커넥션을 닫아줘야한다
            // 외부 리소스를 사용하는건데 안닫아주면 계속 유지된다.
            pstmt.close(); // <- 이 코드에서 exception이 터지면 con 호출이 안됨.
            con.close();
        }
    }
        // statement는 sql을 그대로 넣는것
        // preparestatement는 파라미터랑 같이 sql을 넘길 수 있다.
    private void close(Connection con, Statement stmt, ResultSet rs) throws SQLException {

        if( rs != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                // 스테이트먼트가 null이 아니면 닫아줘라.
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if( con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);

            }
        }
    }
    // 매서드를 빼놓음 getConnection
    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    // Read 회원 하나 조회
    public Member findById(String memberId) throws SQLException {

        String sql = "select * from member where member_id = ?";

        //finally에서 호출해야해서 뺀다
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con =getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            // executeUpdate는 데이터를 변경할 때 사용
            // 조회는 executeQuery를 써야한다.
            // rs는 셀릭트 결과를 가지고 있는 통

            rs = pstmt.executeQuery();
            // next를 해줘야 데이터가 있는 곳부터 본다.
            //
            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId =" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            // exception
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    // update
    public void update(String memberId, int money) throws SQLException {

        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            // 아래처럼 해주면 상단에 sql에 ?,?에 아래 값이 들어간다.
            // 파라미터 바인딩을 한다.
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize = pstmt.executeUpdate(); // int를 반환하는데 영향 받은 DB row 수를 반환한다.
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            // 예외가 나오면 로그를 남김
            log.error("db error", e);
            throw e;
        } finally {
            // 프리페어 스테이츠먼츠와 커넥션을 닫아줘야한다
            // 외부 리소스를 사용하는건데 안닫아주면 계속 유지된다.
            pstmt.close(); // <- 이 코드에서 exception이 터지면 con 호출이 안됨.
            con.close();
        }
    }

    public void delete(String memberId) throws SQLException {

        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.info("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }
}
