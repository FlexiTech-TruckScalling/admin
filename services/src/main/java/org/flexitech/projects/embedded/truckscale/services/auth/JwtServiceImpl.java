package org.flexitech.projects.embedded.truckscale.services.auth;

import java.util.Date;

import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService{

	@Value("${jwt_secret}")
    private String secret;
	
	@Value("${jwt_expiration}")
    private long expiration;

	@Override
	public String generateToken(UserDTO userDTO) {
		return Jwts.builder()
                .setSubject(userDTO.getSessionToken())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
	}

	@Override
	public String extractSessionToken(String token) {
		return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
	}

	@Override
	public boolean validateToken(String token, String sessionToken) {
		return sessionToken.equals(extractSessionToken(token)) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

}
