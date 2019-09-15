package bg.photoapp.api.users.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRepository extends CrudRepository<UserEntity, Long> {
	
	public UserEntity findByEmail(String email);

}
