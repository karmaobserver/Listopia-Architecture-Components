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
        	//With method sendError() we send "text/html", and we need json to able to read message in client
        	//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	response.getWriter().write("Unauthorized, token has expired, please relog");
        	return false;
        }
        System.out.println("Token is valid, handle http request");
        return true;
       
    }

}
