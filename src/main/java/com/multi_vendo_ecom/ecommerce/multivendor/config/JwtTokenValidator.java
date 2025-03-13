package com.multi_vendo_ecom.ecommerce.multivendor.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");

        // Check if the JWT token is present and starts with "Bearer "
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Remove "Bearer " prefix

            try {
                // Generate the signing key
                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());

                // Parse the JWT token
                Claims claims = Jwts.parser()
                        .verifyWith(key) // Use verifyWith instead of setSigningKey
                        .build()
                        .parseSignedClaims(jwt) // Use parseSignedClaims instead of parseClaimsJws
                        .getPayload(); // Use getPayload instead of getBody

                // Extract claims
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert authorities to a list of GrantedAuthority
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create an Authentication object
                Authentication authenticated = new UsernamePasswordAuthenticationToken(email, null, auths);

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticated);

            } catch (Exception e) {
                // Handle invalid JWT tokens
                throw new BadCredentialsException("Invalid JWT token...");
            }
        }


        // Continue the filter chain
        filterChain.doFilter(request, response);





        //        String jwt= request.getHeader("Authorization");
//        //Bearer jwt
//        if(jwt!=null){
//            jwt = jwt.substring(7);
//
//            try{
//                SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
//                Claims claims= Jwts.parserBuilder().setSigningKey(key).build().
//                        parseClaimsJws(jwt).getBody();
//
//                String email = String.valueOf(claims.get("email"));
//                String authorities= String.valueOf(claims.get("authorities"));
//
//                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
//                Authentication authenticated = new UsernamePasswordAuthenticationToken(email, null, auths);
//
//                SecurityContextHolder.getContext().setAuthentication(authenticated);
//
//
//            }catch(Exception e){
//                throw new BadCredentialsException("Invalide JWT token...");
//            }
//        }
    }
}
