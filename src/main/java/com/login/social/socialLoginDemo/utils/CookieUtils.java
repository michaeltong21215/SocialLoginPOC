package com.login.social.socialLoginDemo.utils;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CookieUtils {
    public static Optional<Cookie> getCookie(HttpServletRequest httpServletRequest, String cookieName){
        List<Cookie> cookieList = Arrays.asList(httpServletRequest.getCookies());
        return cookieList.stream().filter(cookie -> cookie.getName().equals(cookieName)).findAny();
    }
}
