package bg.photoapp.api.users.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import bg.photoapp.api.users.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
		
	private Environment env;
	
	private UserService userService;
	
	private BCryptPasswordEncoder bcCryptPasswordEncoder;
	
	@Autowired
	WebSecurity(Environment env, UserService userService,BCryptPasswordEncoder bcCryptPasswordEncoder)
	{
		this.env = env;
		this.userService = userService;
		this.bcCryptPasswordEncoder = bcCryptPasswordEncoder;
	}
	
	private AuthenticationFilter getAuthenticationFilter() throws Exception
	{
		AuthenticationFilter authenticationFilter = new AuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager());
		return authenticationFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
				
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/**").hasIpAddress(env.getProperty("gateway.ip"))
		.and().addFilter(getAuthenticationFilter());
		http.authorizeRequests().antMatchers("/h2-console/**").permitAll().
		and().headers().frameOptions().disable();
		
		super.configure(http);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(bcCryptPasswordEncoder);
	}
	
	
	
	
	

}
