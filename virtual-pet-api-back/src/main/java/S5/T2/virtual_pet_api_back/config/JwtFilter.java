package S5.T2.virtual_pet_api_back.config;

import S5.T2.virtual_pet_api_back.services.JWTService;
import S5.T2.virtual_pet_api_back.services.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-ui.html",
            "/user/login",
            "/user/register"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (EXCLUDED_PATHS.stream().anyMatch(path::startsWith)) {
            log.info("Skipping authentication for public path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
            log.info("Extracted username from token: {}", username);
        } else {
            log.warn("Authorization header is missing or does not start with 'Bearer '.");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("User '{}' is not authenticated. Validating token...", username);

            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
            if (jwtService.validateToken(token, userDetails)) {
                log.info("Token is valid for user '{}'. Setting authentication context.", username);

                String role = jwtService.extractRole(token);
                role = role.toUpperCase().startsWith("ROLE_") ? role.toUpperCase() : "ROLE_" + role;

                Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                log.warn("Invalid token for user '{}'.", username);
            }
        } else if (username == null) {
            log.warn("Username could not be extracted from token. Skipping authentication.");
        }
        log.info("Continuing filter chain for path: {}", path);
        filterChain.doFilter(request, response);
    }
    }

