package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.security.filter.AuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthFilter authFilter;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v*/api-docs/**", "/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .authorizeRequests().antMatchers("/command").hasRole("USER")
                .and()
                .authorizeRequests().antMatchers("/command-list").hasRole("PREMIUM");
    }

}
