package com.example.taskqueue.oauth.currentUser;

import com.example.taskqueue.common.annotation.CurrentUser;
import com.example.taskqueue.user.entity.User;
import com.example.taskqueue.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null;// &&
                //parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication = " + authentication.getPrincipal());
        Optional<User> findUser = userRepository.findByEmail(authentication.getName());
        System.out.println("findUser = " + findUser.get().toString());
        if (findUser.isPresent() && findUser.get().getRefreshToken()!=null) {
            //DB에 사용자가 있고 refresh token이 있다면(로그인 된 상태)
            return findUser.get();
        } else {
            //DB에 사용자는 있지만 refresh token을 null로 업데이트 한 상태라면 로그아웃 한 사용자임.
            log.info("로그인 된 사용자가 없습니다. (혹은 인증되지 않은 사용자 입니다.)");
            return null;
        }
    }
}
