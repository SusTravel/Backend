package org.sustrav.demo.infra;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.sustrav.demo.data.model.UserRole;

@EnableWebSecurity
@Configuration
@Order(1)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private StatelessAuthenticationFilter statelessAuthenticationFilter;


    public ApplicationSecurityConfiguration() {
        super(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().cacheControl();
        http.exceptionHandling();
        http.anonymous().and().servletApi().and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").hasRole(UserRole.USER.getName())
                .antMatchers("/**").permitAll()
                .and()
                // add custom authentication filter for complete stateless JWT based authentication
                .addFilterBefore(statelessAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userService);
    }

}
