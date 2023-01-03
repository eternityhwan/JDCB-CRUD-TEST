package com.hwan.jdbccrud.connection;

// 상수를 변수로 만든 것.
// 객체를 생성해서는 안되기 때문에 abstract로 객체 생성을 막아놨다.
public abstract class ConnectionConst {
    public static final String URL = "jdbc:mysql://localhost:3306/oscboard?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1q2w3e4r";
}
