package oauth.test.oauth2jwt.controller;

import oauth.test.oauth2jwt.dto.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Controller
public class MyController {
    @GetMapping("/my")
    @ResponseBody
    public String myAPI(){
        return "my route";
    }

    @GetMapping("/myInfo")
    @ResponseBody
    public Map<String, ?> myInfo(Authentication authentication) {
        // SecurityContext에서 현재 인증된 사용자 정보 꺼내기
        CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        return Map.of("userId", userDetails.getId(), "role", role);
    }

}
