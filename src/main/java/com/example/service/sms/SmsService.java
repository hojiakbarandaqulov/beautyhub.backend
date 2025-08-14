package com.example.service.sms;

import com.example.dto.sms.SmsAuthDTO;
import com.example.enums.SmsType;
import com.example.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    private final SmsHistoryService smsHistoryService;
    private final ResourceBundleMessageSource messageSource;


    public void sendSmsUpdatePhone(String phone) {
        String code = RandomUtil.getRandomCode();
        String message = "<#>Freya dasturiga Telefon raqamni o'zgartirish uchun tasdiqlash kodi:" + code;
        send(phone, message);
        smsHistoryService.create(phone, message, code, SmsType.RESET_PASSWORD);
    }

    public void sendSms(String phone) {
        String code = RandomUtil.getRandomCode();
        String message = "<#>Freya dasturiga Ro'yhatdan o'tish uchun tasdiqlash kodi:"+code;
        send(phone, message);
        smsHistoryService.create(phone, message, code, SmsType.RESET_PASSWORD);
    }

    private void send(String phone, String message) {
        try {
            String token = getToken();
            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("mobile_phone", phone)
                    .add("message", message)
                    .add("from", "4546")
                    .build();

            Request request = new Request.Builder()
                    .url("https://notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Authorization", "Bearer " + token)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getToken() throws JsonProcessingException {
        SmsAuthDTO smsAuthDTO = new SmsAuthDTO();
        smsAuthDTO.setEmail(myEskizUzEmail);
        smsAuthDTO.setPassword(myEskizUzPassword);
        String response = restTemplate.postForObject(smsUrl + "/auth/login", smsAuthDTO, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response);
        return node.get("data").get("token").asText();
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


