package be.zwaldeck.author.listener;

import be.zwaldeck.zcms.repository.api.exception.RepositoryException;
import be.zwaldeck.zcms.repository.api.service.SetupService;
import be.zwaldeck.zcms.repository.api.service.StubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final SetupService setupService;
    private final Optional<StubService> stubService;

    @Autowired
    public ApplicationStartupListener(SetupService setupService, Optional<StubService> stubService) {
        this.setupService = setupService;
        this.stubService = stubService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            if (stubService.isPresent() && askForStubs()) {
                stubService.get().loadStubs();
            } else if (!setupService.isRepositorySetup()) {
                setupService.setupRepository();
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    private boolean askForStubs() {
        System.out.print("Do you want to load the stubs (Y)es/(N)o: ");
        var scanner = new Scanner(System.in);
        var answer = scanner.next().trim();
        return answer.equalsIgnoreCase("yes") ||
                (answer.length() == 1 && answer.equalsIgnoreCase("y"));
    }
}
