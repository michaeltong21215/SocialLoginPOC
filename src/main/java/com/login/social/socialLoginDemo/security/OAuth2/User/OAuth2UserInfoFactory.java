package com.login.social.socialLoginDemo.security.OAuth2.User;

import com.login.social.socialLoginDemo.exception.UnsupportedOAuth2ProviderException;
import com.login.social.socialLoginDemo.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
   public static OAuth2UserInfo getOAuth2UserInfo(String registerId, Map<String, Object> attributes){
       if(registerId.equals(AuthProvider.GOOGLE.toString())){
           return new GoogleOAuth2UserInfo(attributes);
       } else {
           throw new UnsupportedOAuth2ProviderException("The provided OAuth type is not supported in this application");
       }
   }
}
