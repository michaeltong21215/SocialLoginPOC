package com.login.social.socialLoginDemo.security;

import com.login.social.socialLoginDemo.config.AppProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TokenProvider {

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getExpirationMilliSec());
        return Jwts
                .builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public boolean isValidToken(String token){
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException e) {
            log.error("expired jwt token {}", ExceptionUtils.getStackTrace(e));
        } catch (UnsupportedJwtException e) {
            log.error("unsupported jwt token {}", ExceptionUtils.getStackTrace(e));
        } catch (MalformedJwtException e) {
            log.error("malformed jwt token {}", ExceptionUtils.getStackTrace(e));
        } catch (SignatureException e) {
            log.error("issue with the signature from jwt token {}", ExceptionUtils.getStackTrace(e));
        } catch (IllegalArgumentException e) {
            log.error("illegal argument exception from parsing the token {}", ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    public Long getUserIdFromToken(String token){
        Claims claims = Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
