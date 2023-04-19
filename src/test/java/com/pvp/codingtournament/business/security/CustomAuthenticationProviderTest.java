package com.pvp.codingtournament.business.security;

import com.pvp.codingtournament.business.enums.RoleEnum;
import com.pvp.codingtournament.business.repository.UserRepository;
import com.pvp.codingtournament.business.repository.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class CustomAuthenticationProviderTest {

    @Mock
    PasswordEncoder encoder;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomAuthenticationProvider customAuthenticationProvider;

    UserEntity userEntity;

    @BeforeEach
    void init(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userEntity = new UserEntity(1L, "TestName",
                "TestSurname",
                "Test",
                "test",
                "testEmail@gmail.com",
                "+37065555555",
                0,
                "Beginner",
                RoleEnum.ROLE_USER, Collections.emptySet(),  Collections.emptySet(),  Collections.emptySet());
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
    }

    @Test
    void authenticate_success() {
        String username = "Test";
        String password = "test";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        Mockito.when(encoder.matches(any(), any())).thenReturn(true);
        Authentication authentication = new TestingAuthenticationToken(username, password);
        authentication = customAuthenticationProvider.authenticate(authentication);

        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(username, authentication.getName());
        assertEquals(password, authentication.getCredentials().toString());
        assertEquals("[ROLE_USER]", authentication.getAuthorities().toString());
    }

    @Test
    void authentication_failed(){
        String username = "Test";
        String password = "test";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        Mockito.when(encoder.matches(any(), any())).thenReturn(false);
        Authentication authentication = new TestingAuthenticationToken(username, password);
        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }
}