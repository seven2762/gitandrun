package com.sparta.gitandrun.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.gitandrun.user.jwt.JwtAuthenticationFilter;
import com.sparta.gitandrun.user.jwt.JwtAuthorizationFilter;
import com.sparta.gitandrun.user.jwt.JwtUtil;
import com.sparta.gitandrun.user.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

import static org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl.fromHierarchy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

   private final JwtUtil jwtUtil;
   private final UserDetailsServiceImpl userDetailsService;
   private final AuthenticationConfiguration authenticationConfiguration;

   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
       return configuration.getAuthenticationManager();
   }

   @Bean
   public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
       JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
       filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
       return filter;
   }
    @Bean
    RoleHierarchy roleHierarchy() {
        return fromHierarchy("ROLE_MANAGER > ROLE_ADMIN > ROLE_OWNER > ROLE_CUSTOMER");
    }

    @Bean
   public JwtAuthorizationFilter jwtAuthorizationFilter() {
       return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
   }

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
               .csrf(AbstractHttpConfigurer::disable)
               .formLogin(AbstractHttpConfigurer::disable)
               .httpBasic(AbstractHttpConfigurer::disable)
               .sessionManagement(config ->
                       config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers(
                               "/user/signup",
                               "/user/login",
                               "/swagger-ui/**",
                               "/api-docs/**",
                               "/swagger-ui/**",
                               "/v3/api-docs/**",
                               "/swagger-resources/**"
                       ).permitAll()
                       .anyRequest().authenticated()
               )
               .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
               .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
               .exceptionHandling(handler -> handler
                       .authenticationEntryPoint((request, response, authException) -> {
                           log.error("인증 에러: {}", authException.getMessage());
                           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                           response.setContentType("application/json;charset=UTF-8");

                           ObjectMapper objectMapper = new ObjectMapper();
                           String jsonResponse = objectMapper.writeValueAsString(Map.of
                                   ("code", "FORBIDDEN",
                                   "message", "접근 권한이 없습니다.")
                           );

                           response.getWriter().write(jsonResponse);
                       })
                       .accessDeniedHandler((request, response, accessDeniedException) -> {
                           log.error("접근 거절: {}", accessDeniedException.getMessage());
                           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                           response.setContentType("application/json;charset=UTF-8");


                           ObjectMapper objectMapper = new ObjectMapper();
                           String jsonResponse = objectMapper.writeValueAsString(Map.of("code", "FORBIDDEN",
                                           "message", "접근 권한이 없습니다."));

                           response.getWriter().write(jsonResponse);
                       })
               );

       return http.build();
   }

}
