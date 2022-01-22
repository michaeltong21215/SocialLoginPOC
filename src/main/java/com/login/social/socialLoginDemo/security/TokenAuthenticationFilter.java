package com.login.social.socialLoginDemo.security;

import com.login.social.socialLoginDemo.exception.SocialLoginDemoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AppUserDetailService appUserDetailService;

    public TokenAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(token) && tokenProvider.isValidToken(token)){
                Long userId = tokenProvider.getUserIdFromToken(token);
                UserDetails userDetails = appUserDetailService.getUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.error("Unable to set user principle in security context {}", ExceptionUtils.getStackTrace(e));
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization");

        if(StringUtils.hasText(token) && token.startsWith("Bearer ")){
            return token.substring(7);
        } else {
            throw new SocialLoginDemoException("Bearer token is not available");
        }
    }
}
