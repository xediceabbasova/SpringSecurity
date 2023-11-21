package com.company.springsecurity.basicauth.security;

import com.company.springsecurity.basicauth.model.Role;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security, HandlerMappingIntrospector introspector) throws Exception {

        //h2 console problemini aradan qaldirmag ucun yazmaliyiq
        MvcRequestMatcher.Builder mvcRequestBuilder = new MvcRequestMatcher.Builder(introspector);


        security
                .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(csrfConfig ->
                        csrfConfig.ignoringRequestMatchers(mvcRequestBuilder.pattern("/public/**"))
                                .ignoringRequestMatchers(PathRequest.toH2Console()))
                .authorizeHttpRequests(x -> //h2 console uygun mvcrequestbuilder etdiyimizden hamsini ele vermeliyik
                        x
                                .requestMatchers(mvcRequestBuilder.pattern("/public/**")).permitAll()
                                .requestMatchers(mvcRequestBuilder.pattern("/private/admin/**")).hasRole(Role.ROLE_ADMIN.getValue())
                                .requestMatchers(mvcRequestBuilder.pattern("/private/**")).hasAnyRole(Role.ROLE_FSK.getValue(),
                                        Role.ROLE_USER.getValue(),
                                        Role.ROLE_ADMIN.getValue())
                                .requestMatchers(PathRequest.toH2Console()).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())//disable yox withdefaults dedikde default login logout sehifeleri olur
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(x->x.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        return security.build();
    }

}
