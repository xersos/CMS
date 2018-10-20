package be.zwaldeck.zcms.repository.api.service;

import be.zwaldeck.zcms.repository.api.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {

    User create(User user);
}
