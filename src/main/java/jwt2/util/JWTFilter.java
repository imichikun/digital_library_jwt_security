package jwt2.util;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt2.security.JWTTokenGeneration;
import jwt2.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTTokenGeneration jwtTokenGeneration;
    private final PersonDetailsService personDetailsService;

    @Autowired
    public JWTFilter(JWTTokenGeneration jwtTokenGeneration, PersonDetailsService personDetailsService) {
        this.jwtTokenGeneration = jwtTokenGeneration;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization"); // из запроса к приложению извлекаем header, который
                                                               // называется Authorization
        if (header != null && !header.isBlank() && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);

            if (jwt.isBlank())
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer header");

            try {
                String username = jwtTokenGeneration.validateTokenAndRetrieveClaim(jwt);
                UserDetails userDetails = personDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

                if (SecurityContextHolder.getContext().getAuthentication() == null)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            } catch (JWTVerificationException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
            }
        }

        filterChain.doFilter(request, response);                // передаём фильтер дальше по цепочке
    }
}