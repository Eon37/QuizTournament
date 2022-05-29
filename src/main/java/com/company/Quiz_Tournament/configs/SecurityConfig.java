package com.company.Quiz_Tournament.configs;

import com.company.Quiz_Tournament.configs.UserDetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/actuator/shutdown", "/error/**", "/h2-console/**", "/", "/login").permitAll()
                .antMatchers("/api/quizzes/create", "/api/users/current").authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login-request")
                    .defaultSuccessUrl("/api/quizzes")
                    .passwordParameter("password")
                    .usernameParameter("email")
                    .permitAll()
                .and()
                .logout()
                    .logoutSuccessUrl("/api/quizzes")
                .and()
                .httpBasic()

        ;
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
//        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
//        .usersByUsernameQuery("select email, password, 'true' from users where email = ?")
//        .authoritiesByUsernameQuery("select email, 'USER' from users where email = ?");

        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
