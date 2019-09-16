package bg.zuul.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private final Environment env;
	
	@Autowired
	public WebSecurity(Environment env) {
		this.env = env;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.headers().frameOptions().disable();
		
		//permit all request to the login page without validation
		http.authorizeRequests().antMatchers(HttpMethod.POST, env.getProperty("api.login.url.path")).permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, env.getProperty("api.registration.url.path")).permitAll();
		http.authorizeRequests().antMatchers(env.getProperty("api.h2.console.url.path")).permitAll()
		//this means that any other request different by those above will need to be authenticated
		.anyRequest().authenticated()
		.and()
		.addFilter(new AuthorizationFilter(authenticationManager(), env));
		
		// by this code the authorization header will be cleared and will need to validate the JWT token everytime
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	
	

}
