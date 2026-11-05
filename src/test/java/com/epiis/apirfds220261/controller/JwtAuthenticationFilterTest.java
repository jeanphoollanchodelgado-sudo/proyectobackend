package com.epiis.apirfds220261.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


import java.util.Base64;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import com.epiis.apirfds220261.entity.EntityUser;

import jakarta.servlet.FilterChain;

class JwtAuthenticationFilterTest {

    private static final String SECRET = Base64.getEncoder().encodeToString(
        "test-secret-with-at-least-thirty-two-bytes".getBytes()
    );

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void acceptsValidBearerTokens() throws Exception {
        JwtService jwtService = new JwtService(SECRET, 60_000);
        EntityUser user = new EntityUser();
        user.setIdUser("user-id");
        user.setRole("Encargado");
        String token = jwtService.generateToken(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        new TestableFilter(jwtService).run(request, response, chain);

        assertEquals(
            "user-id",
            SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        assertEquals(
            "ROLE_Encargado",
            SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().iterator().next().getAuthority()
        );
        verify(chain).doFilter(request, response);
    }

    @Test
    void passesRequestsWithoutBearerAndRejectsInvalidTokens() throws Exception {
        JwtService jwtService = new JwtService(SECRET, 60_000);
        TestableFilter filter = new TestableFilter(jwtService);
        FilterChain chain = mock(FilterChain.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        MockHttpServletRequest noHeader = new MockHttpServletRequest();
        filter.run(noHeader, response, chain);
        verify(chain).doFilter(noHeader, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        MockHttpServletRequest invalid = new MockHttpServletRequest();
        invalid.addHeader("Authorization", "Bearer invalid.token.value");
        filter.run(invalid, response, chain);
        assertEquals(401, response.getStatus());
        verify(chain, never()).doFilter(invalid, response);
    }

    private static final class TestableFilter extends JwtAuthenticationFilter {
        private TestableFilter(JwtService jwtService) {
            super(jwtService);
        }

        private void run(
            MockHttpServletRequest request,
            MockHttpServletResponse response,
            FilterChain chain
        ) throws Exception {
            doFilterInternal(request, response, chain);
        }
    }
}
