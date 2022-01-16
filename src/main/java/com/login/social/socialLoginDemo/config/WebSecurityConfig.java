package com.login.social.socialLoginDemo.config;

import com.login.social.socialLoginDemo.security.AppUserDetailService;
import com.login.social.socialLoginDemo.security.OAuth2.AppOAuth2AuthFailureHandler;
import com.login.social.socialLoginDemo.security.OAuth2.AppOAuth2AuthSuccessHandler;
import com.login.social.socialLoginDemo.security.OAuth2.AppOAuth2UserService;
import com.login.social.socialLoginDemo.security.OAuth2.HttpCookieOAuth2AuthorizationRequestRepo;
import com.login.social.socialLoginDemo.security.RestAuthenticationEntryPoint;
import com.login.social.socialLoginDemo.security.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    private TokenAuthenticationFilter getTokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepo getCookieOAuth2AuthorizationRequestRepo(){
        return new HttpCookieOAuth2AuthorizationRequestRepo;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(appUserDetailService).passwordEncoder(getPasswordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable().httpBasic().disable().exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and().authorizeRequests().antMatchers("").permitAll().anyRequest().authenticated()
                .and().oauth2Login().authorizationEndpoint().baseUri("/oauth2/authorize").authorizationRequestRepository(getCookieOAuth2AuthorizationRequestRepo())
                .and().redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and().userInfoEndpoint().userService(appOAuth2UserService)
                .and().successHandler(appOAuth2AuthSuccessHandler)
                .and()
    }
}
