package com.login.social.socialLoginDemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix="App")
public class AppProperties {
    //Todo: implement the configuration properties

    private final Auth auth = new Auth();

    private final OAuth2 oAuth2 = new OAuth2();

    public static class Auth{
        private String tokenSecret;
        private Long expirationMilliSec;

        public String getTokenSecret() {
            return tokenSecret;
        }

        public void setTokenSecret(String tokenSecret) {
            this.tokenSecret = tokenSecret;
        }

        public Long getExpirationMilliSec() {
            return expirationMilliSec;
        }

        public void setExpirationMilliSec(Long expirationMilliSec) {
            this.expirationMilliSec = expirationMilliSec;
        }
    }

    public static class OAuth2{
        private List<String> authorizedRedirectUrisList = new ArrayList();

        public List<String> getAuthorizedRedirectUrisList() {
            return authorizedRedirectUrisList;
        }

        public OAuth2 OAuth2(List<String> authorizedRedirectUrisList) {
            this.authorizedRedirectUrisList = authorizedRedirectUrisList;
            return this;
        }
    }
    public Auth getAuth(){
        return auth;
    }

    public OAuth2 getoAuth2() { return oAuth2; }
}
