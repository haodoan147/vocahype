package com.vocahype.configuration;

import com.vocahype.security.BCryptPasswordEncoderCustom;
import com.vocahype.security.CustomJwtAuthenticationConverter;
import com.vocahype.security.FailAuthenticationEntryPoint;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static com.vocahype.util.Routing.API_PUB;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(Routing.LEARNING_TIME + "/**").authenticated()
                .antMatchers(Routing.TOPICS + "/**").authenticated()
                .antMatchers(Routing.KNOWLEDGE_TEST_50 + "/**").authenticated()
                .antMatchers(Routing.WORDS_LEARN + "/**").authenticated()
                .antMatchers(Routing.PROFILE + "/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(failAuthenticationEntryPoint())
//                .and()
//                .oauth2ResourceServer().jwt()
//                .jwtAuthenticationConverter(customJwtAuthenticationConverter)
        ;

        http.addFilterBefore(firebaseAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public FirebaseAuthenticationFilter firebaseAuthenticationFilter() {
        return new FirebaseAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().antMatchers(API_PUB + "**", "/swagger-ui/**");
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoderCustom();
    }

    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendError(HttpStatus.FORBIDDEN.value(), "No right permission to access resource");
        };
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        };
    }

    @Bean
    public FailAuthenticationEntryPoint failAuthenticationEntryPoint() {
        return new FailAuthenticationEntryPoint();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromIssuerLocation("https://securetoken.google.com/vocahype");
//    }
}
