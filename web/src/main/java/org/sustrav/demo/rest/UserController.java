package org.sustrav.demo.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.sustrav.demo.data.UserRepository;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.services.user.UserAuthentication;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "/api/user/current", method = RequestMethod.GET)
    public User getCurrent() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UserAuthentication) {
            return ((UserAuthentication) authentication).getDetails();
        }
        return null;
    }

/*
    @RequestMapping(value = "/api/facebook/details", method = RequestMethod.GET)
    public org.springframework.social.facebook.api.User getSocialDetails() {
        String[] fields = {"id", "email", "first_name", "last_name"};
        org.springframework.social.facebook.api.User userProfile =
                facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        return userProfile;
    }
*/
}
