package com.gabrielferreira02.roomReservation.service;

import com.gabrielferreira02.roomReservation.entity.Role;
import com.gabrielferreira02.roomReservation.entity.UserEntity;
import com.gabrielferreira02.roomReservation.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Test
    @DisplayName("It should create a user details successfully")
    void loadUserByUsername() {
        UserEntity user = new UserEntity(
                1L,
                "User",
                "17824829707",
                "12345678",
                new HashSet<>(Set.of(Role.USER))
        );

        when(userRepository.findByUsername(anyString())).thenReturn(user);

        UserDetails response = customUserDetailsService.loadUserByUsername("User");
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));

        assertNotNull(response);
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getPassword(), response.getPassword());
        assertEquals(authorities, response.getAuthorities());
    }

    @Test
    @DisplayName("It should fail on create a user details due to invalid username")
    void loadUserByUsernameError() {

        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("asndjd");
        });
    }
}