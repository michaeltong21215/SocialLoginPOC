package com.login.social.socialLoginDemo.exception;

public class UnsupportedOAuth2ProviderException extends RuntimeException{
    public UnsupportedOAuth2ProviderException(String msg){
        super(msg);
    }
}
