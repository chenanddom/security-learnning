package com.itdom.securitylearnning;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Slf4j
@SpringBootTest
class SecurityLearnningApplicationTests {

    @Test
    void contextLoads() {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String afterEncoder = encoder.encode("test123");
        log.info("encoder password:{}",afterEncoder);
        log.info("check password:{}",encoder.matches("test123",afterEncoder));


    }

}
