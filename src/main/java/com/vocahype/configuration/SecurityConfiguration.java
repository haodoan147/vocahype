package com.vocahype.configuration;

import com.vocahype.security.BCryptPasswordEncoderCustom;
import com.vocahype.security.CustomJwtAuthenticationConverter;
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
                .antMatchers(Routing.LEARNING_TIME).authenticated()
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(customJwtAuthenticationConverter);
        return http.build();
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token", "Referer", "Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Access-Control-Allow-Methods", "Access-Control-Allow-Credentials", "Access-Control-Expose-Headers", "Access-Control-Max-Age", "Access-Control-Request-Headers", "Access-Control-Request-Method", "Age", "Allow", "Alternates", "Content-Range", "Content-Disposition", "Content-Description", "Content-Encoding", "Content-Language", "Content-Length", "Content-Location", "Content-MD5", "Content-Transfer-Encoding", "Content-Type", "Date", "Last-Modified", "Link", "Location", "MIME-Version", "Retry-After", "Server", "Transfer-Encoding", "Vary", "WWW-Authenticate", "X-Frame-Options", "X-Powered-By", "X-Requested-With", "X-Content-Type-Options", "X-Forwarded-Proto", "X-XSS-Protection", "X-Application-Context", "X-Cache", "X-Cache-Hits", "X-Cache-Expires", "X-Cache-Key", "X-Cache-Remote", "X-Cache-Debug", "X-Request-ID", "X-Correlation-ID", "X-Real-IP", "X-Forwarded-For", "X-Forwarded-Host", "X-Forwarded-Server", "X-Forwarded-Port", "X-Forwarded-Prefix", "X-Forwarded-Ssl", "X-Forwarded-Protocol", "X-Forwarded-Https", "X-Forwarded-Ip", "X-ProxyUser-Ip", "X-ProxyUser-Realm", "X-ProxyUser-Groups", "X-ProxyUser-Subject", "X-ProxyUser-Session", "X-ProxyUser-AccessToken", "X-ProxyUser-IdToken", "X-ProxyUser-RefreshToken", "X-ProxyUser-Token-Expires-In", "X-ProxyUser-Token-Expires-At", "X-ProxyUser-Session-Expires-In", "X-ProxyUser-Session-Expires-At", "X-ProxyUser-Realm-Access-Token", "X-ProxyUser-Realm-Access-Refresh-Token", "X-ProxyUser-Realm-Access-Token-Expires-In", "X-ProxyUser-Realm-Access-Token-Expires-At", "X-ProxyUser-Realm-Access-Refresh-Token-Expires-In", "X-ProxyUser-Realm-Access-Refresh-Token-Expires-At", "X-Custom-Header"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://securetoken.google.com/vocahype");
    }
}
