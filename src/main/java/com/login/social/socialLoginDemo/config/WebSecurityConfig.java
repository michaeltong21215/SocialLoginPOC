package com.login.social.socialLoginDemo.config;

import com.login.social.socialLoginDemo.security.AppUserDetailService;
import com.login.social.socialLoginDemo.security.OAuth2.AppOAuth2AuthFailureHandler;
import com.login.social.socialLoginDemo.security.OAuth2.AppOAuth2AuthSuccessHandler;
import com.login.social.socialLoginDemo.security.OAuth2.AppOAuth2UserService;
import com.login.social.socialLoginDemo.security.OAuth2.HttpCookieOAuth2AuthorizationRequestRepo;
import com.login.social.socialLoginDemo.security.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //configureGlobal
    //configure
    //passwordEncoder
    //tokenAuthFilter
    @Autowired
    private AppUserDetailService appUserDetailService;

    @Autowired
    private AppOAuth2UserService appOAuth2UserService;

    @Autowired
    private AppOAuth2AuthSuccessHandler appOAuth2AuthSuccessHandler;

    @Autowired
    private AppOAuth2AuthFailureHandler appOAuth2AuthFailureHandler;

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepo httpCookieOAuth2AuthorizationRequestRepo;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;
}
