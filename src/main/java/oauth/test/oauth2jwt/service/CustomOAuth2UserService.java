package oauth.test.oauth2jwt.service;

import lombok.RequiredArgsConstructor;
import oauth.test.oauth2jwt.dto.*;
import oauth.test.oauth2jwt.entity.UserEntity;
import oauth.test.oauth2jwt.repository.UserRepository;
import oauth.test.oauth2jwt.util.UserMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = switch(registrationId) {
            case "naver" -> new NaverResponse(oAuth2User.getAttributes());
            case "google" -> new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
            default -> null;
        };

        String username = oAuth2Response.getProvider() + "@" + oAuth2Response.getProviderId();

        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            user = registerOAuthUser(username, oAuth2Response);
        } else {
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());
            userRepository.save(user);
        }

        return new CustomOAuth2User(UserMapper.toDTO(user));
    }

    private UserEntity registerOAuthUser(String username, OAuth2Response oAuth2Response) {
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .email(oAuth2Response.getEmail())
                .name(oAuth2Response.getName())
                .role("ROLE_USER")
                .build();

        userRepository.save(userEntity);
        return userEntity;
    }
}
