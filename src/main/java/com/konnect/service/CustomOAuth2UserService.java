package com.konnect.service;

import com.konnect.dto.*;
import com.konnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.konnect.entity.UserEntity;
import com.konnect.util.UserMapper;
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

        String oauthCode = oAuth2Response.getProvider() + "@" + oAuth2Response.getProviderId();

        UserEntity user = userRepository.findByOauthCode(oauthCode);

        if (user == null) {
            user = registerOAuthUser(oauthCode, oAuth2Response);
        } else {
            user.setName(oAuth2Response.getName());
            user.setEmail(oAuth2Response.getEmail());
            userRepository.save(user);
        }

        return new CustomUserPrincipal(UserMapper.toDTO(user));
    }

    private UserEntity registerOAuthUser(String oauthCode, OAuth2Response oAuth2Response) {
        UserEntity userEntity = UserEntity.builder()
                .name(oAuth2Response.getName())
                .email(oAuth2Response.getEmail())
                .password(null)
                .role("ROLE_USER")
                .oauthCode(oauthCode)
                .build();

        userRepository.save(userEntity);
        return userEntity;
    }
}
