package com.mvc.auth.controller;

import com.mvc.auth.service.AuthService;
import com.mvc.auth.util.user.JwtAuthenticationRequest;
import com.mvc.auth.util.user.JwtAuthenticationResponse;
import com.mvc.auth.util.user.JwtUserAuthenticationRequest;
import com.mvc.common.msg.Result;
import com.mvc.common.msg.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("jwt")
public class AuthController {
    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public Result<String> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws Exception {
        final String token = authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return ResultGenerator.genSuccessResult(token);
    }

    @RequestMapping(value = "user/token", method = RequestMethod.POST)
    public Result<String> createUserAuthenticationToken(
            @RequestBody JwtUserAuthenticationRequest authenticationRequest) throws Exception {
        final String token = authService.loginUser(authenticationRequest.getUserName(), authenticationRequest.getClientId(), authenticationRequest.getUserId(), authenticationRequest.getAddress());
        return ResultGenerator.genSuccessResult(token);
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(
            HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        }
    }

    @RequestMapping(value = "verify", method = RequestMethod.GET)
    public ResponseEntity<?> verify(String token) throws Exception {
        authService.validate(token);
        return ResponseEntity.ok(true);
    }

    @RequestMapping(value = "invalid", method = RequestMethod.POST)
    public ResponseEntity<?> invalid(@RequestHeader("access-token") String token){
        authService.invalid(token);
        return ResponseEntity.ok(true);
    }

}
