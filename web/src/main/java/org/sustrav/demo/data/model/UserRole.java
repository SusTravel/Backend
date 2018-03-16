package org.sustrav.demo.data.model;


public enum UserRole {

    USER, ADMIN, TECH_ADMIN;

    private static final String ROLE_PREFIX = "ROLE_";

    public static UserRole valueOf(final UserAuthority authority) {
        for (UserRole role : UserRole.values()) {
            if (authority.getAuthority().equals(ROLE_PREFIX + role.getName()))
                return role;

        }

        throw new IllegalArgumentException("No role defined for authority: " + authority.getAuthority());
    }

    public UserAuthority asAuthorityFor(final User user) {
        final UserAuthority authority = new UserAuthority();
        authority.setAuthority(ROLE_PREFIX + getName());
        authority.setUser(user);
        return authority;
    }

    public String getName() {
        return toString();
    }

    public String getAuthority() {
        return ROLE_PREFIX + getName();
    }

}
