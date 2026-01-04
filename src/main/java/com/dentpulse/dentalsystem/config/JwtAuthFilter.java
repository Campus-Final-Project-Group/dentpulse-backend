package com.dentpulse.dentalsystem.config;

import com.dentpulse.dentalsystem.entity.User;
import com.dentpulse.dentalsystem.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    //  ADD THIS for forget password
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        // ✅ Skip JWT filter for auth endpoints
        if (path.startsWith("/api/v1/auth/")) {
            return true;
        }

        // ✅ Skip JWT filter for AI recommendation endpoint
//        if (path.equals("/api/appointments/ai-recommendation")) {
//            return true;
//        }
        if (path.startsWith("/api/appointments/ai-recommendation")) { // ← ADD THIS LINE
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                String email = jwtUtil.extractEmail(token);
                User user = userRepo.findByEmail(email);

                if (user != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user, null, user.getAuthorities());

                    auth.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception ignored) {}
        }

        // ✅ Continue filter chain (important!)
        filterChain.doFilter(request, response);
    }
}
