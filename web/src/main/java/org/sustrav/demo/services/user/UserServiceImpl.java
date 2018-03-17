package org.sustrav.demo.services.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sustrav.demo.data.UserRepository;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.data.model.UserRole;
import org.sustrav.demo.services.fb.FBUser;

import java.util.Date;
import java.util.List;

import static org.sustrav.demo.MainApplication.LOGGER;
import static org.sustrav.demo.services.fb.FacebookService.FACEBOOK_PROVIDER;

@Service
@Transactional
public class UserServiceImpl implements UserService, ApplicationListener<ApplicationReadyEvent> {

    public static final String ADMIN_ENGINE = "AdminEngine";
    private static final String INTERNAL_PROVIDER = "internal";
    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenAuthenticationService authenticationService;

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUserId(String userId) {
        final User user = userRepo.findById(Long.valueOf(userId));
        return checkUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        final User user = userRepo.findByUsername(username);
        return checkUser(user);
    }


    @Override
    public void updateUserDetails(User user) {
        userRepo.save(user);
    }

    private User checkUser(User user) {
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    @Override
    public User createInternalUser(String userName, UserRole role) {
        User user = new User();
        user.setProviderId(INTERNAL_PROVIDER);
        user.setProviderUserId(userName);
        user.setUsername(userName);
        user.setFbToken("unknown");
        user.setAccessToken(authenticationService.createTokenForUser(user));
        userRepo.save(user);

        user.grantRole(role);
        userRepo.saveAndFlush(user);

        return user;
    }

    @Override
    public List<User> getInternalUsers(UserRole role) {
        return userRepo.findUsersByAuthority(role.getAuthority(), INTERNAL_PROVIDER);
    }

    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        User techAdmin = null;
        try {
            techAdmin = loadUserByUsername(ADMIN_ENGINE);
        } catch (UsernameNotFoundException ex) {
            techAdmin = createInternalUser(ADMIN_ENGINE, UserRole.TECH_ADMIN);
        }

        LOGGER.info("Tech user has token {}", techAdmin.getAccessToken());
    }

    @Override
    public User getUserByToken(FBUser fbUser, boolean orCreate) {
        User user = userRepo.findByProviderIdAndProviderUserId(FACEBOOK_PROVIDER, fbUser.getId());

        if (user == null && orCreate) {
            user = new User();
            user.setUsername(generateUniqueUserName(fbUser.getFirstName()));
            user.setLastName(fbUser.getLastName());
            user.setFirstName(fbUser.getFirstName());
            user.setEmail(fbUser.getEmail());
            user.setProviderId(FACEBOOK_PROVIDER);
            user.setFbToken(fbUser.getAccessToken());
            user.setProviderUserId(fbUser.getId());
            user.grantRole(UserRole.USER);
            user.setExpires(new Date().getTime() + TokenAuthenticationService.TEN_DAYS);
            user = userRepo.save(user);
            user.setAccessToken(authenticationService.createTokenForUser(user));
            userRepo.saveAndFlush(user);
        }

        return user;
    }

    private String generateUniqueUserName(final String firstName) {
        String username = getUsernameFromFirstName(firstName);
        String option = username;
        for (int i = 0; userRepo.findByUsername(option) != null; i++) {
            option = username + i;
        }
        return option;
    }

    private String getUsernameFromFirstName(final String userId) {
        final int max = 25;
        int index = userId.indexOf(' ');
        index = index == -1 || index > max ? userId.length() : index;
        index = index > max ? max : index;
        return userId.substring(0, index);
    }
}
