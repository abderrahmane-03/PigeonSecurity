    package net.yc.race.track.config;

    import com.nimbusds.jose.jwk.RSAKey;
    import com.nimbusds.jose.jwk.source.JWKSource;
    import com.nimbusds.jose.proc.SecurityContext;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.oauth2.jwt.JwtDecoder;
    import org.springframework.security.oauth2.jwt.JwtEncoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
    import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.web.cors.CorsConfiguration;
    import org.springframework.web.cors.CorsConfigurationSource;
    import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

    import jakarta.servlet.http.HttpServletResponse;
    import java.util.Arrays;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        private final UserDetailsService userDetailsService;

        @Value("${cors.allowed-origins:http://localhost:3000}")
        private String allowedOrigins;

        public SecurityConfig(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/public/**").permitAll()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/breeder/**").hasRole("BREEDER")
                            .requestMatchers("/organizer/**").hasRole("ORGANIZER")
                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
                    .exceptionHandling(exceptionHandling -> exceptionHandling
                            .authenticationEntryPoint(authenticationEntryPoint())
                            .accessDeniedHandler(new CustomAccessDeniedHandler())
                    )
                    .requiresChannel(channel -> channel.anyRequest().requiresSecure());

            return http.build();
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authenticationManagerBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);
            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
            return authenticationManagerBuilder.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

        @Bean
        public AuthenticationEntryPoint authenticationEntryPoint() {
            return (request, response, authException) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Authentication is required to access this resource.");
            };
        }

        @Bean
        public JwtEncoder jwtEncoder(RsaKeyProperties rsaKeyProperties) {
            RSAKey rsaKey = new RSAKey.Builder(rsaKeyProperties.getPublicKey())
                    .privateKey(rsaKeyProperties.getPrivateKey())
                    .build();
            JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(new com.nimbusds.jose.jwk.JWKSet(rsaKey));
            return new NimbusJwtEncoder(jwkSource);
        }


        @Bean
        public JwtDecoder jwtDecoder() {
            return NimbusJwtDecoder.withJwkSetUri("https://example.com/your-jwks-set.json").build();
        }
    }
