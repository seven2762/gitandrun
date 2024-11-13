package com.sparta.gitandrun.common.config;

import com.sparta.gitandrun.user.jwt.JwtAuthenticationFilter;
import com.sparta.gitandrun.user.jwt.JwtAuthorizationFilter;
import com.sparta.gitandrun.user.jwt.JwtUtil;
import com.sparta.gitandrun.user.security.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
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
                               "/user/login"
                       ).permitAll()
                       .requestMatchers("/user/password").hasAnyRole("CUSTOMER","OWNER")
                       .requestMatchers("/user/all").hasAnyRole("MANGER","ADMIN")
                       .requestMatchers("/user/delete").hasRole("MANAGER")
                       .requestMatchers("/admin/**").hasRole("MANAGER")
                       .requestMatchers("/stores/**").hasAnyRole("OWNER", "MANAGER", "ADMIN")
                       .anyRequest().authenticated()
               )
               .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
               .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
               .exceptionHandling(handler -> handler
                       .authenticationEntryPoint((request, response, authException) -> {
                           log.error("인증 에러: {}", authException.getMessage());
                           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                           response.setContentType("application/json;charset=UTF-8");

                           String jsonResponse = String.format(
                                   "{\"code\":\"%s\",\"message\":\"%s\"}",
                                   "UNAUTHORIZED",
                                   "인증되지 않은 요청입니다."
                           );

                           response.getWriter().write(jsonResponse);
                       })
                       .accessDeniedHandler((request, response, accessDeniedException) -> {
                           log.error("접근 거절: {}", accessDeniedException.getMessage());
                           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                           response.setContentType("application/json;charset=UTF-8");

                           String jsonResponse = String.format(
                                   "{\"code\":\"%s\",\"message\":\"%s\"}",
                                   "FORBIDDEN",
                                   "접근 권한이 없습니다."
                           );

                           response.getWriter().write(jsonResponse);
                       })
               );

       return http.build();
   }

}
