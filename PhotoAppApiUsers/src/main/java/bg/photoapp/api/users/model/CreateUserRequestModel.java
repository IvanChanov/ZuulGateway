package bg.photoapp.api.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateUserRequestModel {
	
	@NotNull(message="FirstName is required field, cannot be null")
	@Size(min=2, message="FirstName must not be less than 2 characters")
	private String firstName;
	
	@NotNull(message="LastName is required field, cannot be null")
	@Size(min=2, message="LastName must not be less than 2 characters")
	private String lastName;
	
	@NotNull(message="Password is required field, cannot be null")
	@Size(min=8, message="Password must not be MIN 8 characters and MAX 16 characters")
	private String password;
	
	@NotNull(message="Email is required field, cannot be null")
	@Email
	private String email;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
