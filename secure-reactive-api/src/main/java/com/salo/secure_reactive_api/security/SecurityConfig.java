package com.salo.secure_reactive_api.security;

import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // ✅ Converter REACTIVO (Jwt -> Mono<AbstractAuthenticationToken>)
        Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthConverter =
                new ReactiveJwtAuthenticationConverter();

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(ex -> ex
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/**").authenticated()
                        .anyExchange().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                                .jwtAuthenticationConverter(reactiveJwtAuthConverter)
                        )
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        return NimbusReactiveJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * ✅ Clase explícita (NO lambda) para evitar que Spring intente registrarla
     * como Converter global de WebFlux y falle con genéricos.
     */
    static class ReactiveJwtAuthenticationConverter
            implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

        private final JwtGrantedAuthoritiesConverter authoritiesConverter;

        ReactiveJwtAuthenticationConverter() {
            JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
            gac.setAuthoritiesClaimName("roles");

            // Opción A (recomendada): tu JWT trae "ROLE_ADMIN"
            gac.setAuthorityPrefix("");

            // Si tu JWT trae "ADMIN" (sin ROLE_), cambia a:
            // gac.setAuthorityPrefix("ROLE_");

            this.authoritiesConverter = gac;
        }

        @Override
        public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
            return Mono.just(new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt)));
        }
    }
}