package com.aleksej.makaji.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

@Component
public class ListopiaInterceptor extends HandlerInterceptorAdapter {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    		
		System.out.println("ListopiaInterceptor");
		
		String header = request.getHeader("Authorization");
        System.out.println("Header is: " + header);
        
        TokenVerifier tokenVerifier = new TokenVerifier();
        GoogleIdToken token = tokenVerifier.verifyToken(header);
        
        if (token == null) {
        	System.out.println("Token is null, sendError");
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        	return false;
        }
        System.out.println("Token is valid, handle http request");
        return true;
       
    }

}
