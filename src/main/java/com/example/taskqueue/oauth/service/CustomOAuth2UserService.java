package com.example.taskqueue.oauth.service;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.oauth.CustomOAuth2User;
import com.example.taskqueue.oauth.OAuthAttributes;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.entity.state.CatState;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Getter
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private static final String KAKAO = "kakao";
    private String accessToken; // 카카오에서 제공하는 access token
    private User createdUser;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");
        /**
         * 카카오에서 제공해주는 access token은 아래와 같이 확인 가능함.
         * */
        accessToken = userRequest.getAccessToken().getTokenValue();
        System.out.println("카카오에서 제공한 accessToken = " + accessToken);
        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        System.out.println("oAuth2User.toString() = " + oAuth2User.getAttributes());

        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.ofKakao(userNameAttributeName, attributes);
        createdUser = getUser(extractAttributes); // getUser() 메소드로 User 객체 생성 후 반환
        System.out.println("extractAttributes ID = " + extractAttributes.getOauth2UserInfo().getId());
        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
    }


    private User getUser(OAuthAttributes attributes) {
        /**
         * findBySocialId db에서 찾은다음에 해당 유저의 email을 업데이트
         * */
        User findUser = userRepository.findBySocialId(attributes.getOauth2UserInfo().getId()).orElse(null);
        if (findUser == null) {
            return saveUser(attributes);
        }else{
            findUser.updateEmail(attributes.getOauth2UserInfo().getEmail());
            userRepository.save(findUser);
            return findUser;
        }
    }

    /**
     * OAuthAttributes의 toEntity() 메소드를 통해 빌더로 User 객체 생성 후 반환
     * 생성된 User 객체를 DB에 저장 : socialType, socialId, email, role 값만 있는 상태
     */
    private User saveUser(OAuthAttributes attributes) {
        User createdUser = attributes.toEntity(attributes.getOauth2UserInfo());
        createdUser.updateRunStreak(0);
        createdUser.updateThemeColor("#ABC123");
        createdUser.updateDailyUpdate(false);
        createdUser.updateIntro("");
        createdUser.updateCatState(CatState.FOUR);
        return userRepository.save(createdUser);
    }
}