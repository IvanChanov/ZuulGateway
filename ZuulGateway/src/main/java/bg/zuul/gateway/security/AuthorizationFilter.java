package bg.zuul.gateway.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	Environment env;
	
	// by default needs to have a constructor
	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env) {
		super(authenticationManager);
		this.env = env;
	}

	
	//Overwriting this method because needs to perform filtering on the Tokens
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		//will get the auth header
		String authorizationHeader = request.getHeader(env.getProperty("authorization.token.header.name"));
		
		//will chech if exists and if contains the correct prefix witch is Bearer 
		if(authorizationHeader == null || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix")))
		{
			//if not will filter and return
			chain.doFilter(request, response);
			return;
		}
		
		//if contains will perform authorization
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}


	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		
		//get the header
		String authorizationHeader = request.getHeader(env.getProperty("authorization.token.header.name"));
		
		//if null return
		if(authorizationHeader == null)
		{
			return null;
		}
		
		//replace the Bearer prefix with empty string in order to have clean value of the token
		String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"), "");
		
		//parse the token and use the same Sign key that was used when the token was initially created
		String userId = Jwts.parser()
						.setSigningKey(env.getProperty("token.secret"))
						.parseClaimsJws(token)
						.getBody()
						.getSubject();
	
		if(userId == null)
			return null;
		
		return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
	}
	
	
	
	

}
