package com.example.service.sms;

import com.example.dto.base.ApiResult;
import com.example.entity.SmsHistoryEntity;
import com.example.enums.LanguageEnum;
import com.example.enums.SmsType;
import com.example.exp.AppBadException;
import com.example.repository.SmsHistoryRepository;
import com.example.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class SmsHistoryService {
    private final SmsHistoryRepository smsHistoryRepository;
    private final ResourceBundleMessageSource messageSource;

     private static final int smsCountLimit = 3;


/*     public void sendRegistrationSms(String phoneNumber) {
         String smsCode = RandomUtil.getRandomCode();
         String text = "<#>beautyhub.uz partali. Ro'yxatdan o'tish uchun tasdiqlash kodi: " + smsCode;
         sendMessage(phoneNumber, text, smsCode);
     }

     public void sendMessage(String phone, String text, String smsCode, LanguageEnum language) {
         Long countSms = smsHistoryRepository.countByPhoneAndCreatedDateBetween(phone,
                 LocalDateTime.now().minusMinutes(2),
                 LocalDateTime.now());
         if (countSms < smsCountLimit) {

             SmsHistoryEntity smsHistory = new SmsHistoryEntity();
             smsHistory.setCode(smsCode);
             smsHistory.setAttemptCount(0);
             smsHistory.setSmsType(SmsType.REGISTRATION);
             smsHistory.setPhone(phone);
             smsHistory.setMessage(text);

             SmsHistoryEntity sms = smsHistoryRepository.save(smsHistory);
             smsSenderService.get().sendSmsHTTP(sms);
             return;
         }
         throw new AppBadException(messageSource.getMessage("sms.limit.over",null,new Locale(language.name())));
     }
     public ApiResponse<String> checkSmsCode(String phone, String code) {
         Optional<SmsHistoryEntity> optional = smsHistoryRepository.findTopByPhoneOrderByCreatedDateDesc(phone);
         if (optional.isEmpty()) {
             log.warn("Phone Incorrect! Phone = {}, code = {}", phone, code);
             return new ApiResponse<>(resourceMessageService.getMessage("sms.code.incorrect"), 400, true);
         }

         SmsHistoryEntity entity = optional.get();
         if (entity.getCreatedDate().plusMinutes(2L).isBefore(LocalDateTime.now())) {
             log.warn("Sms Code Incorrect! Phone = {}, code = {}", phone, code);
             smsHistoryRepository.updateStatus(entity.getId(), SmsStatus.USED_WITH_TIMEOUT);
             return new ApiResponse<>(resourceMessageService.getMessage("sms.time-out"), 400, true);
         }
         if (!entity.getCode().equals(code)) {
             return new ApiResponse<>(resourceMessageService.getMessage("sms.code.incorrect"), 400, true);
         }
         smsHistoryRepository.updateStatus(entity.getId(), SmsStatus.IS_USED);
         return new ApiResponse<>("Success!", 200, false);
     }

     public void sendResetSms(String phone, String signature) {
         if (signature == null) {
             signature = "";
         }
         String smsCode = RandomUtil.getRandomCode();
 //        String text = "Scolaro.uz partali - parolni qayta tiklash uchun tasdiqlash kodi: " + smsCode;
         String text = "<#>interview.uz partali. Ro'yxatdan o'tish uchun tasdiqlash kodi: " + smsCode + "\n" + signature;
         sendMessage(phone, text, smsCode);
     }*/

    public String create(String toPhone, String message, String code, SmsType smsType) {
        SmsHistoryEntity smsHistoryEntity = new SmsHistoryEntity();
        smsHistoryEntity.setPhone(toPhone);
        smsHistoryEntity.setMessage(message);
        smsHistoryEntity.setCode(code);
        smsHistoryEntity.setSmsType(smsType);
        smsHistoryEntity.setCreatedDate(LocalDateTime.now());
        smsHistoryRepository.save(smsHistoryEntity);
        return null;
    }

    public void checkPhoneLimit(String phone) { // 1 minute -3 attempt
        // 23/05/2024 19:01:13
        // 23/05/2024 19:01:23
        // 23/05/2024 19:01:33

        // 23/05/2024 19:00:55 -- (current -1)
        // 23/05/2024 19:01:55 -- current

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(2);

        Long count = smsHistoryRepository.countByPhoneAndCreatedDateBetween(phone, from, to);
        if (count >= 3) {
            throw new AppBadException("Sms limit reached. Please try after some time");
        }
    }

    public void isNotExpiredPhone(String phone) {
        Optional<SmsHistoryEntity> optional = smsHistoryRepository.findTop1ByPhoneOrderByCreatedDateDesc(phone);
        if (optional.isEmpty()) {
            throw new AppBadException("Phone history not found");
        }
        SmsHistoryEntity entity = optional.get();
        if (entity.getCreatedDate().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new AppBadException("Confirmation time expired");
        }
    }

    public  Boolean checkSmsCode(String phone, String code) {
        Optional<SmsHistoryEntity> optional = smsHistoryRepository.findTop1ByPhoneOrderByCreatedDateDesc(phone);
        if (optional.isEmpty()) {
           return false;

        }
        SmsHistoryEntity entity = optional.get();
        if (entity.getCreatedDate().plusMinutes(2L).isBefore(LocalDateTime.now())) {
            return false;
        }
        if (!entity.getCode().equals(code)) {
           return false;
        }
        smsHistoryRepository.updateStatus(entity.getId(), SmsType.CONFIRM_RESET_PASSWORD);
        return  true;
    }
}
