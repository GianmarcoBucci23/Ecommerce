package com.ecommerce.project.security.jwt;


import com.ecommerce.project.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.ecom.app.jwtCookie}")
    private String jwtCookie;

    //getting Jwt From Header
//    public String getJwtFromHeader(HttpServletRequest request){
//
//        String bearerToken = request.getHeader("Authorization");// prendo dall'header Authorization
//        logger.debug("Authorization bearer: {}" , bearerToken);
//        if(bearerToken != null && bearerToken.startsWith("Bearer")){
//            return bearerToken.substring(7);//tolgo bearer in modo tale che mi rimane solo il token
//        }
//        return null;
//    }

    public String getJwtFromCookies(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request,jwtCookie);
        if (cookie != null){
            return cookie.getValue();
        }else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie (UserDetailsImpl userDetails){
        String jwt = generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,jwt)
                .path("/api")
                .maxAge(24*60*60)
                .httpOnly(false)
                .build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie () {
        return ResponseCookie.from(jwtCookie,null)
                .path("/api")
                .build();
    }

    //generate token from username
    public String generateTokenFromUsername(String username){
//        String username = userDetails.getUsername();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }


    //getting username form jwtToken
    public String getUsernameFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //generate signin key
    public Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //validate jwtToken
    public boolean validateJwtToken(String authToken){
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);

            return true;
        }catch (MalformedJwtException e){
            logger.debug("Invalid JWT Token {}",e.getMessage());
        }catch (ExpiredJwtException e){
            logger.debug("Jwt Token expired {}",e.getMessage());
        }catch(UnsupportedJwtException e){
            logger.debug("JWT Token is unsupported {}",e.getMessage());
        }catch(IllegalArgumentException e){
            logger.debug("JWT claims is empty {}",e.getMessage());
        }

        return false;
    }
}

