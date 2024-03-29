package bg.photoapp.api.users.ui.controllers;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bg.photoapp.api.users.dto.UserDto;
import bg.photoapp.api.users.model.CreateUserRequestModel;
import bg.photoapp.api.users.model.CreateUserResponseModel;
import bg.photoapp.api.users.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

	@Autowired
	private Environment env;

	@Autowired
	UserService userService;

	@GetMapping("/status/check")
	public String status() {
		return "Working... on port " + env.getProperty("local.server.port") + " and secret token used is " + env.getProperty("token.secret") ;
	}

	
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, 
						      MediaType.APPLICATION_JSON_VALUE }, 
			     produces = {
							  MediaType.APPLICATION_XML_VALUE, 
							  MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		CreateUserResponseModel retunValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

		return new ResponseEntity<CreateUserResponseModel>(retunValue, HttpStatus.OK);
	}

}
