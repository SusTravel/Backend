package org.sustrav.demo.services.user;

import org.springframework.transaction.annotation.Transactional;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.data.model.UserRole;
import org.sustrav.demo.services.fb.FBUser;

import java.util.List;

public interface UserService {
    @Transactional(readOnly = true)
    User loadUserByUserId(String userId);

    @Transactional(readOnly = true)
    User loadUserByUsername(String username);

    void updateUserDetails(User user);

    User createInternalUser(String userName, UserRole role);

    @Transactional(readOnly = true)
    List<User> getInternalUsers(UserRole role);

    User getUserByToken(FBUser fbUser, boolean orCreate);
}
