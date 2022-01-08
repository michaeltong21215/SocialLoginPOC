package com.login.social.socialLoginDemo.security.OAuth2;

import com.login.social.socialLoginDemo.exception.OAuth2AuthProcessingException;
import com.login.social.socialLoginDemo.exception.UnsupportedOAuth2ProviderException;
import com.login.social.socialLoginDemo.model.AuthProvider;
import com.login.social.socialLoginDemo.model.User;
import com.login.social.socialLoginDemo.repository.UserRepository;
import com.login.social.socialLoginDemo.security.OAuth2.User.OAuth2UserInfo;
import com.login.social.socialLoginDemo.security.OAuth2.User.OAuth2UserInfoFactory;
import com.login.social.socialLoginDemo.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class AppOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User.getAttributes());
    }

    private User updateUser(User existingUser, OAuth2UserInfo oAuth2UserInfo){
        existingUser.setUsername(oAuth2UserInfo.getName());
        return userRepository.save(existingUser);
    }

    private User registerUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo){
        //String username, String email, String password, String providerId
        User newuser = new User(
                oAuth2UserInfo.getName(),
                oAuth2UserInfo.getEmail(),
                "",
                oAuth2UserInfo.getId(),
                AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        //Todo: How to use enum in our application
        return userRepository.save(newuser);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, Map<String, Object> attributes) {
        String OAuth2Type = userRequest.getClientRegistration().getClientId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(OAuth2Type, attributes);

        if(ObjectUtils.isEmpty(oAuth2UserInfo.getEmail())){
            throw new OAuth2AuthProcessingException("There is no email id found within the Auth Provider");
        }

        Optional<User> user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User currentUser = null;
        if(user.isPresent()) {
            String providerId = user.get().getProviderId();
            if(!providerId.equals(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))) {
                throw new UnsupportedOAuth2ProviderException("The given request is signing in with the wrong OAuth Provider");
            }
            currentUser = user.get();
            currentUser = updateUser(currentUser, oAuth2UserInfo);
        } else {
            currentUser = registerUser(userRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(currentUser, attributes);
    }
}
