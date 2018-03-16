package org.sustrav.demo.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.sustrav.demo.data.model.UserAuthority;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, UserAuthority> {
    @Modifying
    @Query("delete from UserAuthority ua where ua.user.id= ?1")
    void deleteForUser(Long userId);
}
