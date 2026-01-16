//package com.magvy.experis.javalava_backend.infrastructure.repositories;
//
//import com.magvy.experis.javalava_backend.domain.entitites.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.simple.JdbcClient;
//import org.springframework.stereotype.Repository;
//
//public clas
//@Repository
//public class test {
//    @Autowired
//    private JdbcClient jdbcClient;
//
//    public test() {
//
//    }
//
//    public boolean userExists(String username) {
//        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
//        return jdbcClient.sql(query)
//                .param(username)
//                .query(rs -> {
//                    if (rs.next()) {
//                        int count = rs.getInt(1);
//                        return count > 0;
//                    }
//                    return false;
//                });
//    }
//
//    public User getUser(String username) {
//        String query = "SELECT * FROM users WHERE username = ?";
//        return jdbcClient.sql(query)
//                .param(username)
//                .withMaxRows(1)
//                .query();
//    }
//
//    public void createUser(User user) {
//        String query = "INSERT INTO users (username, hashed_password, full_name, email, age, birthday) VALUES (?, ?, ?, ?, ?, ?)";
//        jdbcClient.sql(query)
//                .param(user.getUsername())
//                .param(user.getHashedPassword())
//                .param(user.getFullName())
//                .param(user.getEmail())
//                .param(user.getAge())
//                .param(user.getBirthday())
//                .update();
//    }
//}
