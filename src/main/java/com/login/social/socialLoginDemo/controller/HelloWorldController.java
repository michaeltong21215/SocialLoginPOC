package com.login.social.socialLoginDemo.controller;

import com.login.social.socialLoginDemo.exception.BadRequestException;
import com.login.social.socialLoginDemo.payload.SignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/hello")
@Slf4j
public class HelloWorldController {
    //Todo: create a get resource, accepts a signuprequest payload. If the payload has all the mandatary information, it sends a success message.
    // Else if mandatary attributes are missing then we throw a bad request exception
    //success message: request is received successfully
    @GetMapping
    public ResponseEntity<?> getRequest(@RequestBody SignupRequest signupRequest){
        if(!ObjectUtils.isEmpty(signupRequest.getEmail())
                && !ObjectUtils.isEmpty(signupRequest.getPassword())
        && !ObjectUtils.isEmpty(signupRequest.getUsername())) {
            return new ResponseEntity("request is received successfully", HttpStatus.CREATED);
        } else {
            throw new BadRequestException("request is either missing email, password or username");
        }
    }
}
