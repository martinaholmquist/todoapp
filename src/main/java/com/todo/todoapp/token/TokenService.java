package com.todo.todoapp.token;

import com.todo.todoapp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    public void deleteTokensByUser(User user) {
        tokenRepository.deleteByUser(user);
    }
}
