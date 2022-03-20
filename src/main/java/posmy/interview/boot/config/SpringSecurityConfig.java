package posmy.interview.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DataSource dataSource;
    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetails member =
                User.withUsername("member")
                        .password(bCryptPasswordEncoder.encode("password"))
                        .roles("MEMBER")
                        .build();
        UserDetails librarian =
                User.withUsername("librarian")
                        .password(bCryptPasswordEncoder.encode("password"))
                        .roles("LIBRARIAN")
                        .build();
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //HTTP Basic authentication
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/books").hasAnyRole("LIBRARIAN", "MEMBER")
                .antMatchers(HttpMethod.GET, "/books/**").hasAnyRole("LIBRARIAN", "MEMBER")
                .antMatchers(HttpMethod.POST, "/books").hasRole("LIBRARIAN")
                .antMatchers(HttpMethod.PUT, "/books/**").hasRole("LIBRARIAN")
                .antMatchers(HttpMethod.PATCH, "/books/**").hasRole("MEMBER")
                .antMatchers(HttpMethod.DELETE, "/books/**").hasRole("LIBRARIAN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}
