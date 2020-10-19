package com.casestudy.config;

import com.casestudy.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) userServiceImpl)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/**","/css/**","/css_2/**","/documentation/**","/javascript/**","/fonts/**","/images/**","/img/**","/javascript/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/cart").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/home/listUser").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/admin/").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/product/create/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/product/list/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/product/delete-product/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/").access("hasRole('ROLE_USER')")
                .and()
                .formLogin()
                .loginPage("/home/login")
                .loginProcessingUrl("/authenticateTheUser")
                .defaultSuccessUrl("/product", true)
                .usernameParameter("name").passwordParameter("password")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/home/logout"))
//                    .logoutSuccessUrl("/home")
                .permitAll()
                .and().exceptionHandling().accessDeniedPage("/error");
        http.csrf().disable();
    }
}
