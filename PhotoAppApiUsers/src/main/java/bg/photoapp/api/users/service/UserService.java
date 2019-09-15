package bg.photoapp.api.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import bg.photoapp.api.users.dto.UserDto;

public interface UserService extends UserDetailsService	{
	
	public UserDto createUser(UserDto userDetails);

}
