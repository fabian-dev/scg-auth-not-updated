package scg.authnotupdated;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

    @GetMapping(path = "/me", produces = "application/json")
    public Mono<User> user(@AuthenticationPrincipal Authentication auth) {

        if (auth instanceof OAuth2AuthenticationToken oauth
                && oauth.isAuthenticated()
                && oauth.getPrincipal() instanceof OidcUser principal) {

            var name = principal.getClaims().get("preferred_username");
            return Mono.just(new User(name.toString()));
        }
        return Mono.just(User.ANONYMOUS);
    }

    record User(String name) {
        static final User ANONYMOUS = new User("");
    }

}
