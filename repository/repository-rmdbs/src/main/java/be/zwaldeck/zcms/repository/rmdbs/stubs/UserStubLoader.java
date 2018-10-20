package be.zwaldeck.zcms.repository.rmdbs.stubs;

import be.zwaldeck.zcms.repository.api.model.Role;
import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.rmdbs.service.UserServiceRMDBS;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class UserStubLoader {

    private UserStubLoader() {
    }

    public static List<User> load(PasswordEncoder passwordEncoder, UserServiceRMDBS userService) {
        var users = new ArrayList<User>();

        var author = new User();
        author.setUsername("author");
        author.setPassword(passwordEncoder.encode("author"));
        author.setRoles(Arrays.asList(Role.ROLE_AUTHOR, Role.ROLE_DAM_MGR, Role.ROLE_PUBLIC));

        var damMgr = new User();
        damMgr.setUsername("dam");
        damMgr.setPassword(passwordEncoder.encode("dam"));
        damMgr.setRoles(Arrays.asList(Role.ROLE_DAM_MGR, Role.ROLE_PUBLIC));

        users.add(userService.create(author));
        users.add(userService.create(damMgr));

        return users;
    }
}
