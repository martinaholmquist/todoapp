package com.todo.todoapp.Schedules;


import com.todo.todoapp.token.Token;
import com.todo.todoapp.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenCleanUpTask {

/*
    private TokenRepository tokenRepository;
    //@Scheduled(fixedRate = 180000) // 180000 millisekunder = 3 minuter
      //Scheduled(fixedRate = 1800000) // 1800000 millisekunder = 30 minuter
    //@Scheduled(fixedRate = 900000) // 900000 millisekunder = 15 minuter
    public void cleanupExpiredTokens() {
        List<Token> expiredTokens = tokenRepository.findAllExpiredTokens();

        for (Token expiredToken : expiredTokens) {
            expiredToken.setExpired(true);
            expiredToken.setRevoked(true);
        }
        tokenRepository.saveAll(expiredTokens);
    }*/

}
