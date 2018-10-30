package be.zwaldeck.zcms.repository.rmdbs.service;

import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.model.Site;
import be.zwaldeck.zcms.repository.api.model.User;
import be.zwaldeck.zcms.repository.api.service.SetupService;
import be.zwaldeck.zcms.repository.api.service.StubService;
import be.zwaldeck.zcms.repository.rmdbs.stubs.SiteStubLoader;
import be.zwaldeck.zcms.repository.rmdbs.stubs.UserStubLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("stubs")
@Service
public class StubServiceRMDBS implements StubService {

    private final PasswordEncoder passwordEncoder;
    private final SetupService setupService;
    private final UserServiceRMDBS userService;
    private final SiteServiceRMDBS siteService;

    @Autowired
    public StubServiceRMDBS(PasswordEncoder passwordEncoder, SetupService setupService,
                            UserServiceRMDBS userService, SiteServiceRMDBS siteService) {
        this.passwordEncoder = passwordEncoder;
        this.setupService = setupService;
        this.userService = userService;
        this.siteService = siteService;
    }

    private List<User> users;
    private List<Site> sites;

    @Override
    public void loadStubs() throws RepositoryException {
        setupService.setupRepository();

        users = UserStubLoader.load(passwordEncoder, userService);
        sites = SiteStubLoader.load(siteService);

        cleanUp();
    }

    private void cleanUp() {
        users = null;
    }
}
