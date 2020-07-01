package com.psl.app.security;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.psl.app.model.BlackListedTokens;
import com.psl.app.model.Constants;
import com.psl.app.repo.BlackListedTokensRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	BlackListedTokensRepository repo;

	public String getUsernameFromToken(String token) throws Exception{
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) throws Exception{
        return getClaimFromToken(token, Claims::getExpiration);
    }
    
    public String getJtiFromToken(String token) throws Exception {
    	return getClaimFromToken(token,Claims::getId);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) throws Exception{
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) throws Exception{
        return Jwts.parser()
                .setSigningKey(Constants.SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) throws Exception{
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Authentication authentication) {
    	
    	final String authorities = authentication.getAuthorities().toArray()[0].toString();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(Constants.AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS256, Constants.SIGNING_KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .setId(UUID.randomUUID().toString())
                .compact();
    }

    public void validateToken(String token, UserDetails userDetails) throws Exception{
        final String username = getUsernameFromToken(token);
       BlackListedTokens blacklisted= repo.findByJti(this.getJtiFromToken(token));
//        return ( username.equals(userDetails.getUsername())
//                    && !isTokenExpired(token) && (blacklisted==null));
        
       if(!( username.equals(userDetails.getUsername()) && !isTokenExpired(token) && (blacklisted==null)) )
    	   throw new Exception("blacklisted token");
    	  
    }


}
