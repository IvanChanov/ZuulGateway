package bg.photoapp.api.users.service.impl;

import java.util.ArrayList;
import java.util.UUID;

import org.hibernate.engine.spi.Mapping;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import bg.photoapp.api.users.data.UserEntity;
import bg.photoapp.api.users.data.UserRepository;
import bg.photoapp.api.users.dto.UserDto;
import bg.photoapp.api.users.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	UserRepository userRepository;
	BCryptPasswordEncoder bcCryptPasswordEncoder;
	
	@Autowired
	UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bcCryptPasswordEncoder)
	{
		this.userRepository = userRepository;
		this.bcCryptPasswordEncoder = bcCryptPasswordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bcCryptPasswordEncoder.encode(userDetails.getPassword()));
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
	
		userRepository.save(userEntity);
		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
		
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity entity =  userRepository.findByEmail(username);
		if(entity == null)
		{
			throw new UsernameNotFoundException(username);
		}
		
		return new User(entity.getEmail(),entity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity entity =  userRepository.findByEmail(email);
		if(entity == null)
		{
			throw new UsernameNotFoundException(email);
		}
		
		return new ModelMapper().map(entity, UserDto.class);
	}
}
