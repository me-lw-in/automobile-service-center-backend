package com.example.serviceassistantbackend.config;

import com.example.serviceassistantbackend.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // SUPER ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/register",
                                "/api/vehicles"
                                ).hasRole("SUPER_ADMIN")

                        // SERVICE_MANAGER or SERVICE_INCHARGE
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/job-cards"
                        ).hasAnyRole("SERVICE_INCHARGE","SERVICE_MANAGER")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/job-cards/my-job-cards"
                        ).hasAnyRole("SERVICE_INCHARGE","SERVICE_MANAGER")

                        //Mechanic
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/job-cards/start"
                        ).hasAnyRole("MECHANIC")
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/job-cards/mechanic",
                                "/api/job-cards/mechanic/assigned-jobs"
                        ).hasAnyRole("MECHANIC")
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/job-problems/**"
                        ).hasAnyRole("MECHANIC"
                        ).requestMatchers(
                                HttpMethod.POST,
                                "/api/job-cards/*/services",
                                "/api/job-cards/*/parts",
                                "/api/job-cards/*/complete"
                        ).hasAnyRole("MECHANIC")

                        // everyone except customer
                        .requestMatchers(
                                HttpMethod.GET,
                                    "/api/services",
                                "/api/parts"
                        ).hasAnyRole("SUPER_ADMIN","SERVICE_INCHARGE","SERVICE_MANAGER", "MECHANIC")

                        .anyRequest().permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling( c ->{
                    c.authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value()));
                });

        return http.build();
    }


    // This bean defines the CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from your React development server
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://192.168.0.105:5173"));
        // Allow all common HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // Allow all headers, which is important for sending the JWT token
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        // Allow credentials like cookies and the Authorization header
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this configuration to all API endpoints
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
