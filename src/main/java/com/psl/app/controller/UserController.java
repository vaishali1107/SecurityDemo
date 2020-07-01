package com.psl.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.psl.app.model.BlackListedTokens;
import com.psl.app.model.LoginUser;
import com.psl.app.model.TokenResponse;
import com.psl.app.repo.BlackListedTokensRepository;
import com.psl.app.security.TokenUtil;

@RestController
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenUtil tokenUtil;
	
	@Autowired
	BlackListedTokensRepository repo;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@GetMapping(value="/")
	public String getHome()
	{
		return "home";
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value="/admin")
	public String getAdmin()
	{
		return "admin";
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping(value="/users")
	public String getUsers()
    {
		return "user";
    }
	
	
	@PostMapping(value="/signin")
	public ResponseEntity<?> signIn(@RequestBody LoginUser user,HttpServletRequest request)
	{
		Authentication authentication=null;
		try {
		
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (AuthenticationException e) {
			return ResponseEntity.ok().body("user password incorrect");
		}


		String uname;
		
		if((uname= (String) request.getAttribute("uname"))!=null)
		{
			if(uname.equals(user.getUsername()))
			{
				String token = request.getHeader("Authorization").substring(7);
				return ResponseEntity.ok().body(new TokenResponse(token));
				
			}
		}

		return ResponseEntity.ok().body(new TokenResponse(tokenUtil.generateToken(authentication)));

	}
	
	@PostMapping(value="/signout")
	public ResponseEntity<?> logout(HttpServletRequest request)
	{
		
		String token = request.getHeader("Authorization").substring(7);
		try {
		repo.save(new BlackListedTokens(tokenUtil.getJtiFromToken(token) ,tokenUtil.getExpirationDateFromToken(token)));
		
		}catch(Exception e)
		{}
		return ResponseEntity.ok().body("logged out successfully");
	}
	
}
