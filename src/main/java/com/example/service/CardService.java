/*
package com.example.service;

import com.example.dto.CardCreateDto;
import com.example.dto.CardDto;
import com.example.entity.Card;
import com.example.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Service
public class CardService {

    private CardRepository cardRepository;

    @Transactional
    public CardDto create(CardCreateDto dto) {
        if (cardRepository.existsByNumber(dto.getNumber())) {
            throw new RuntimeException("Card number already exists");
        }

        Card card = new Card();
        card.setNumber(dto.getNumber());
        card.setHolder(dto.getHolder());
        card.setBalance(dto.getBalance() != null ? dto.getBalance() : 0.0);

        card = cardRepository.save(card);

        // Verify kod yaratish va jo'natish
        String verifyCode = generateVerificationCode();
        verificationCodes.put(card.getId(), verifyCode);

        // SMS yuborish, hozircha logga chiqaramiz
        System.out.println("Send SMS to " + dto.getPhoneNumber() + ": Your verification code is " + verifyCode);

        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setNumber(card.getNumber());
        cardDto.setHolder(card.getHolder());
        cardDto.setBalance(card.getBalance());
        return cardDto;
    }

    public boolean verifyCode(Long cardId, String code) {
        String storedCode = verificationCodes.get(cardId);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(cardId);
            return true;
        }
        return false;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}


public CardDto getById(Long id, LanguageEnum languageEnum) {
        CardEntity card = cardRepository.findById(id).orElseThrow(() -> new RuntimeException("Card not found"));

        CardDto cardDto = new CardDto();
        cardDto.setId(card.getId());
        cardDto.setNumber(card.getNumber());
        cardDto.setHolder(card.getHolder());
        cardDto.setBalance(card.getBalance());

        return cardDto;
    }
}

*/
