package com.vocahype.configuration;

import com.vocahype.dto.enumeration.RoleTitle;
import com.vocahype.repository.RoleRepository;
import com.vocahype.repository.UserRepository;
import com.vocahype.security.BCryptPasswordEncoderCustom;
import com.vocahype.security.FailAuthenticationEntryPoint;
import com.vocahype.util.GeneralUtils;
import com.vocahype.util.Routing;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

import static com.vocahype.util.Routing.API_PUB;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, Routing.WORD_ID + "/**").authenticated()
                .antMatchers(HttpMethod.GET, Routing.WORD).authenticated()
                .antMatchers(HttpMethod.GET, Routing.TOPICS).authenticated()
                .antMatchers(HttpMethod.GET, Routing.TOPIC_ID).authenticated()
                .antMatchers(Routing.LEARNING_TIME + "/**").authenticated()
                .antMatchers(Routing.TOPICS).hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .antMatchers(Routing.WORD ).hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .antMatchers(Routing.KNOWLEDGE_TEST_50 + "/**").authenticated()
                .antMatchers(Routing.WORDS_LEARN + "/**").authenticated()
                .antMatchers(Routing.PROFILE + "/**").authenticated()
                .antMatchers(Routing.RESET_LEARNING_PROGRESSION + "/**").authenticated()
                .antMatchers(Routing.REPORT + "/**").authenticated()
                .antMatchers(Routing.IMPORT + "**").hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .antMatchers(Routing.FETCH_DICTIONARY + "/**").hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .antMatchers(Routing.WORD_ID + "/**").authenticated()
                .antMatchers(Routing.WORD_USER_KNOWLEDGE + "/**").authenticated()
                .antMatchers(Routing.WORD_ID).hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .antMatchers(Routing.TOPIC_ID + "/**").hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .antMatchers(Routing.USER_ID + "/**").hasAnyAuthority(RoleTitle.CONTRIBUTOR.getTitle(), RoleTitle.ADMIN.getTitle())
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(firebaseAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(failAuthenticationEntryPoint());
        return http.build();
    }

    @Bean
    public FirebaseAuthenticationFilter firebaseAuthenticationFilter() {
        return new FirebaseAuthenticationFilter(userRepository, roleRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(API_PUB + "**", "/swagger-ui/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoderCustom();
    }

//    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            GeneralUtils.sendFailAuthenticationBody(response, accessDeniedException.getMessage(), accessDeniedException);
        };
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        };
    }

//    @Bean
    public FailAuthenticationEntryPoint failAuthenticationEntryPoint() {
        return new FailAuthenticationEntryPoint();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://securetoken.google.com/vocahype");
    }
}
