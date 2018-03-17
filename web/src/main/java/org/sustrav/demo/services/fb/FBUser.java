package org.sustrav.demo.services.fb;

import com.restfb.types.User;

public class FBUser {
    String id, email, firstName, lastName, accessToken;

    public FBUser(User user, String token) {
        this(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), token);
    }

    public FBUser(String id, String email, String firstName, String lastName, String accessToken) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessToken = accessToken;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
