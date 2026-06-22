package com.example.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secreto;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth")
            || path.startsWith("/swagger-ui")
            || path.contains("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            onError(response, "Token de autenticación faltante o con formato inválido.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);

            String path = request.getRequestURI();
            String method = request.getMethod();

            if (roles.contains("CLIENTE") && !method.equals("GET")) {
                onError(response, "Lo sentimos, tu cuenta de CLIENTE no tiene permisos para realizar esta acción.", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (roles.contains("VENDEDOR") && method.equals("DELETE")) {
                onError(response, "Lo sentimos, tu cuenta de VENDEDOR no tiene permisos para eliminar registros.", HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String rolesStr = String.join(",", roles);

            HttpServletRequestWrapperWithHeaders mutada = new HttpServletRequestWrapperWithHeaders(request);
            mutada.addHeader("X-User-Username", username);
            mutada.addHeader("X-User-Roles", rolesStr);

            filterChain.doFilter(mutada, response);

        } catch (Exception e) {
            onError(response, "El token JWT ha expirado o su firma es inválida.", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void onError(HttpServletResponse response, String err, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        String httpError = (status == 401) ? "Unauthorized" : "Forbidden";
        String jsonResponse = String.format(
            "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"mensaje\": \"%s\"}",
            LocalDateTime.now(),
            status,
            httpError,
            err
        );

        response.getWriter().write(jsonResponse);
    }
}