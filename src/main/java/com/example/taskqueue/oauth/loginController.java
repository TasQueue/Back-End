package com.example.taskqueue.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class loginController {

    private final kakaoService kakaoService;
    /*@GetMapping("/home")
    public String mainPage(){
        return "login";
    }*/

    @ResponseBody
    @GetMapping("/login")
    public void kakaoCallback(@RequestParam String code){
        System.out.println("code = " + code);
        String KakaoAccessToken = kakaoService.getKakaoAccessToken(code);
        System.out.println("kaKaoAccessToken = " + KakaoAccessToken);
        kakaoService.createKakaoUser(KakaoAccessToken);
    }


}
