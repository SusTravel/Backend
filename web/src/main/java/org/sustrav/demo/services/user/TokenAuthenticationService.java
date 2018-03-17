package org.sustrav.demo.services.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.sustrav.demo.data.model.User;
import org.sustrav.demo.utils.TokenHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

@Service
public class TokenAuthenticationService {

    public static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";
    public static final String AUTH_COOKIE_NAME = "AUTH-TOKEN";
    public static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 10;

    private final TokenHandler tokenHandler;

    @Autowired
    public TokenAuthenticationService(@Value("${security.token.secret}") String secret) {
        tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
    }

    public UserAuthentication getAuthentication(HttpServletRequest request) {
        // to prevent CSRF attacks we still only allow authentication using a custom HTTP header
        // (it is up to the client to read our previously set cookie and put it in the header)
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }

    public void provideAuthentication(HttpServletResponse response, User user) {
        Cookie authCookie = new Cookie(AUTH_COOKIE_NAME, user.getAccessToken());
        authCookie.setPath("/");
        response.addCookie(authCookie);
        response.setHeader(TokenAuthenticationService.AUTH_HEADER_NAME, user.getAccessToken());
    }


    public String createTokenForUser(User user) {
        return tokenHandler.createTokenForUser(user);
    }
}

