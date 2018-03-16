package org.sustrav.demo.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.sustrav.demo.data.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findById(Long id);

    User findByFbToken(String accessToken);

    User findByProviderIdAndProviderUserId(String providerId, String providerUserId);

    @Query("select distinct a.user from UserAuthority a where a.authority = ?1 and a.user.providerId = ?2")
    List<User> findUsersByAuthority(String authority, String providerId);

}
