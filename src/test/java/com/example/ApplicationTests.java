package com.example;

import com.example.service.sms.SmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private SmsService smsService;

    @Test
    void contextLoads() {
//        smsService.getToken();
    }

}
