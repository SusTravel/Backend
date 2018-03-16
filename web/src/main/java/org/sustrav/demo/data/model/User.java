package org.sustrav.demo.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "user_account", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class User {

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    public List<VisitedPlace> visitedPlaces = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @JsonIgnore
    private String providerId;
    @NotNull
    @JsonIgnore
    private String providerUserId;
    @NotNull
    @JsonIgnore
    private String accessToken;
    @NotNull
    @JsonIgnore
    private String fbToken;
    @NotNull
    @Size(min = 4, max = 30)
    private String username;
    @Transient
    private long expires;
    @NotNull
    private boolean accountExpired;
    @NotNull
    private boolean accountLocked;
    @NotNull
    private boolean credentialsExpired;
    @NotNull
    private boolean accountEnabled;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<UserAuthority> authorities;
    @Column
    private int points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public String getUserId() {
        return id.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public Set<UserAuthority> getAuthorities() {
        return authorities;
    }

    // Use Roles as external API
    public Set<UserRole> getRoles() {
        Set<UserRole> roles = EnumSet.noneOf(UserRole.class);
        if (authorities != null) {
            for (UserAuthority authority : authorities) {
                roles.add(UserRole.valueOf(authority));
            }
        }
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        for (UserRole role : roles) {
            grantRole(role);
        }
    }

    public void grantRole(UserRole role) {
        if (authorities == null) {
            authorities = new HashSet<UserAuthority>();
        }
        authorities.add(role.asAuthorityFor(this));
    }

    public void revokeRole(UserRole role) {
        if (authorities != null) {
            authorities.remove(role.asAuthorityFor(this));
        }
    }

    public boolean hasRole(UserRole role) {
        return authorities.contains(role.asAuthorityFor(this));
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return !accountEnabled;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getUsername();
    }

    @JsonIgnore
    public String getPassword() {
        throw new IllegalStateException("password should never be used");
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<VisitedPlace> getVisitedPlaces() {
        return visitedPlaces;
    }

    public void setVisitedPlaces(List<VisitedPlace> visitedPlaces) {
        this.visitedPlaces = visitedPlaces;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }
}
