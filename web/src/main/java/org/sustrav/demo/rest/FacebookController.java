package org.sustrav.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.services.fb.FBUser;
import org.sustrav.demo.services.fb.FacebookService;
import org.sustrav.demo.services.user.TokenAuthenticationService;
import org.sustrav.demo.services.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FacebookController {

    public static final String FB_CONTROLLER_PATH = "/auth/facebook";

    @Autowired
    private UserService userService;

    @Autowired
    private FacebookService facebookService;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @RequestMapping(value = FB_CONTROLLER_PATH, method = RequestMethod.GET)
    public ResponseEntity<Map> authenticateUserByToken(HttpServletRequest httpRequest, HttpServletResponse response) {

        String fbToken = httpRequest.getParameter(FacebookService.FB_TOKEN);
        if (StringUtils.isEmpty(fbToken))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        FBUser fbUser = facebookService.getMetadata(fbToken);
        if (fbUser == null)
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);

        User user = userService.getUserByToken(fbUser, true);

        response.setHeader(HttpHeaders.LOCATION, ServletUriComponentsBuilder.fromContextPath(httpRequest).toUriString());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        tokenAuthenticationService.provideAuthentication(response, user);
        Map data = new HashMap();
        data.put(TokenAuthenticationService.AUTH_HEADER_NAME, user.getAccessToken());
        data.put("user", user);
        return new ResponseEntity<Map>(data, HttpStatus.OK);
    }


}
