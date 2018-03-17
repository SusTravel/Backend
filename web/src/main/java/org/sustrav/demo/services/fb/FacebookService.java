package org.sustrav.demo.services.fb;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.sustrav.demo.rest.FacebookController;

import javax.servlet.http.HttpServletRequest;

@Service
public class FacebookService {

    public static final String FB_TOKEN = "access_token";
    public static final String FACEBOOK_PROVIDER = "facebook";
    private static final Version version = Version.VERSION_2_5;
    private static final String oauthUrl = "https://www.facebook.com/" + version.getUrlElement() + "/dialog/oauth";
    private static final String FAKE_TOKEN = "fake_token";

    @Value("${facebook.appKey}")
    private String appKey;

    @Value("${facebook.appSecret}")
    private String appSecret;

    @Value("${facebook.appNamespace}")
    private String appNamespace;

    @Value("${facebook.mockToken}")
    private boolean mockToken;

    public FBUser getMetadata(String accessToken) {
        // for testing
        if (FAKE_TOKEN.equals(accessToken) && mockToken)
            return new FBUser("117571", "user@gmail.com", "first_name", "last_name", FAKE_TOKEN);

        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, version);
        User user = facebookClient.fetchObject("me", User.class,
                Parameter.with("fields", "name,id,first_name,middle_name,email"));

        return new FBUser(user, accessToken);
    }

    public String getOauthLink(HttpServletRequest httpRequest) {
        String myUrl = ServletUriComponentsBuilder.fromContextPath(httpRequest)
                .path(FacebookController.FB_CONTROLLER_PATH).toUriString();

        String oauthLink = UriComponentsBuilder.fromHttpUrl(oauthUrl)
                .queryParam("client_id", appKey)
                .queryParam("response_type", "token")
                .queryParam("redirect_uri", myUrl).build().toUriString();

        return oauthLink;
    }

}
