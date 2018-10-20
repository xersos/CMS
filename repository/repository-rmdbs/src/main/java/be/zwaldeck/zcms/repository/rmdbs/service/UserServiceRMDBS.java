package be.zwaldeck.zcms.repository.rmdbs.service;

import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.api.service.UserService;
import be.zwaldeck.zcms.repository.rmdbs.converter.UserConverterRMDBS;
import be.zwaldeck.zcms.repository.rmdbs.repository.UserRMDBSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceRMDBS implements UserService {

    private final UserConverterRMDBS userConverter;
    private final UserRMDBSRepository repository;

    @Autowired
    public UserServiceRMDBS(UserConverterRMDBS userConverter, UserRMDBSRepository repository) {
        this.userConverter = userConverter;
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return userConverter.fromDB(repository.saveAndFlush(userConverter.toDB(user, false)));
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userConverter.fromDB(
                repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username '" + username + "'"))
        );
    }
}
