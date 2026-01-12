package com.magvy.experis.springboot_demo.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.magvy.experis.springboot_demo.domain.DTOs.LoginDTO;

@RestController
public class LoginController {
    // @Autowired
    // NamedParameterJdbcTemplate jdbcTemplate;

    @GetMapping("/login")
    public ResponseEntity <String> LoginHandler() {
        return ResponseEntity.ok("Welcome to login page.");
    }

    @PostMapping("/login")
    public ResponseEntity <String> LoginHandler(@RequestBody LoginDTO loginDTO) {
        // Confirm user exists
        // If not, return 401
        // Confirm password is correct
        // If not, return 401
        System.out.println(loginDTO);
        return ResponseEntity.ok("User successfully logged in.");
    }

    // // SQL sample
    // @RequestMapping("calc")
    // Result calc(@RequestParam int left, @RequestParam int right) {
    //     MapSqlParameterSource source = new MapSqlParameterSource()
    //             .addValue("left", left)
    //             .addValue("right", right);
    //     return jdbcTemplate.queryForObject("SELECT :left + :right AS answer", source,
    //             (rs, rowNum) -> new Result(left, right, rs.getLong("answer")));
    // }
}
