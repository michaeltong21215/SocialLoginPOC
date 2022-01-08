package com.login.social.socialLoginDemo.security.OAuth2;

import com.login.social.socialLoginDemo.config.AppProperties;
import com.login.social.socialLoginDemo.exception.BadRequestException;
import com.login.social.socialLoginDemo.security.TokenProvider;
import com.login.social.socialLoginDemo.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Optional;

@Service
@Slf4j
public class AppOAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REDIRECT_URL_COOKIE_NAME = "redirect_uri";

    private TokenProvider tokenProvider;

    private AppProperties appProperties;

    public AppOAuth2AuthSuccessHandler(TokenProvider tokenProvider, AppProperties appProperties) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
    }

    public AppOAuth2AuthSuccessHandler(String defaultTargetUrl, TokenProvider tokenProvider, AppProperties appProperties) {
        super(defaultTargetUrl);
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
    }

    private boolean isAuthorizedRedirectUri(String uri){
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getoAuth2().getAuthorizedRedirectUrisList().stream().anyMatch(authorizedRedirectUri -> {
            URI authorizedUri = URI.create(authorizedRedirectUri);
            if(authorizedUri.getHost().equals(clientRedirectUri.getHost()) &&
               authorizedUri.getPort() == clientRedirectUri.getPort()){
                return true;
            }
            return false;
        });
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URL_COOKIE_NAME).map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())){
            throw new BadRequestException("We got an unauthorized redirect uri and system can't proceed with the authentication");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()){
            log.debug(MessageFormat.format("Response is already sent, unable to redirect to the target url {0}", targetUrl));
        }
        //Todo: clear authentication attributes
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
