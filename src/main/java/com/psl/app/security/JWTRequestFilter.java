package com.psl.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	    String uri=request.getRequestURI();
	
      String header =request.getHeader("Authorization");
      String token=null;
      String username=null;
      
      if(header!=null && header.startsWith("Bearer"))
      {
    	  token=header.substring(7);
    	  try {
			username=tokenUtil.getUsernameFromToken(token);
		} catch (Exception e) {
			if(!uri.equals("/signin")){
				response.reset();
				response.setStatus(HttpStatus.FORBIDDEN.value());
				response.getWriter().write("Invalid token");
				return;
				
			}
			
		}
      }
      if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
      {
    	  UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
    	
			try {
				
				tokenUtil.validateToken(token, userDetails);
				request.setAttribute("uname",username);
				
				  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
						  new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				  SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
	
			catch (Exception e) {
				if(!uri.equals("/signin")){
				response.reset();
				response.setStatus(HttpStatus.FORBIDDEN.value());
				response.getWriter().write("Invalid token");
				return;
				
			}
				
			}
			 
      }
	
    
      filterChain.doFilter(request,response);
	}

}
