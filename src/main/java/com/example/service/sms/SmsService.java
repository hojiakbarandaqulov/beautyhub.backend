package com.example.service.sms;

import com.example.dto.sms.SmsAuthDTO;
import com.example.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
@Service
public class SmsService {
    @Value("${eskiz.url}")
    private String smsUrl;
    @Value("${my.eskiz.uz.email}")
    private String myEskizUzEmail;

    @Value("${my.eskiz.uz.password}")
    private String myEskizUzPassword;
    private final RestTemplate restTemplate;
    private final ResourceBundleMessageSource messageSource;

    public void sendSms(String phone) {
//        String code = RandomUtil.getRandomSmsCode();
        String message = "This is test from Eskiz";
        send(phone, message);
    }

    private void send(String phone, String message) {
        try {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("mobile_phone", phone)
                .add("message", message)
                .add("from", "4546")
                .build();

        Request request = new Request.Builder()
                .url("https://notify.eskiz.uz/api/message/sms/send")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTQ1NDEzNjEsImlhdCI6MTc1MTk0OTM2MSwicm9sZSI6InRlc3QiLCJzaWduIjoiNzczMTlmZDI5ODdiMWRhYTVjMzcwOGU3ODQ2NGZiNzNjN2I4NzcyMTA4NzZkYjk5MTM4YTU5MmIwYmU3MDAyYiIsInN1YiI6IjExNTQ0In0.b4ZLovkzEQJ8F6UW3iwxORr6E2zgkk2U8dPfgVtJAAg")
                .post(body)
                .build();
        Call call = client.newCall(request);
            Response response=call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken(){
        SmsAuthDTO smsAuthDTO = new SmsAuthDTO();
        smsAuthDTO.setEmail(myEskizUzEmail);
        smsAuthDTO.setPassword(myEskizUzPassword);
        String response= restTemplate.postForObject(smsUrl+"/auth/login", smsAuthDTO, String.class);
        System.out.println(response);
        return response;
    }
  /*  public void sentResetPasswordPhone(String username, LanguageEnum language) {
        String code = RandomUtil.getRandomCode();
        String subject = "Reset password Conformation";
        String template = messageSource.getMessage("confirm.code.reset.password",null,new Locale(language.name()));
        template = String.format(template+": "+code);
        checkAndSendMineEmail(username, subject, template, code, language);
    }

    private void checkAndSendMineEmail(String email, String subject, String body, String code, LanguageEnum language) {
        Long count = emailHistoryService.getEmailCount(email);
        if (count >= 3) {
            throw new AppBadException(resourceBundleService.getMessage("email.reached.sms", language));
        }
        sendMimeEmail(email, subject, body);
        emailHistoryService.create(email, code, SmsType.RESET_PASSWORD);
    }*/
}


