package com.example.user_service.controller;

import com.example.user_service.dto.SignInDto;
import com.example.user_service.dto.SignUpDto;
import com.example.user_service.entity.RedisMember;
import com.example.user_service.login.CustomUserDetails;
import com.example.user_service.service.AuthService;
import com.example.user_service.service.MemberService;
import com.example.user_service.service.RedisMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;
    private final RedisMemberService redisMemberService;

    @PostMapping("/test")
    private String test(){
        return "test okkkk";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody SignUpDto signUpDto){
        String username = signUpDto.getUsername();
        String password = signUpDto.getPassword();
        ResponseEntity<?>result = memberService.signUp(username,password);
        log.info("회원가입 결과 :"+result);
        return result;
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();

        Authentication authentication = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Map<String, String> responseBody = new HashMap<>();
        //로그인 성공시 생성된 사용자 고유번호와 이름을 레디스에 저장해야함.
        if(authentication.isAuthenticated()){
            RedisMember saved = redisMemberService.save(new RedisMember(customUserDetails.getIdx(), authentication.getName()));
            //레디스에 저장된 데이터의 id를 반환. 이제 이것을 토큰에 담아줄 예정.
            log.info("인증성공. idx: "+saved.getIdx()+ "name: "+saved.getUsername());

            responseBody.put("redisKey", saved.getId());
            return ResponseEntity.ok(responseBody);
        }
        return ResponseEntity.badRequest().body(responseBody);
    }

    /*
    @GetMapping("/get-username/{idx}")
    public ResponseEntity<String> getUsername(@PathVariable String idx) {
        try {
            // RedisTestService를 호출하여 특정 필드 조회
            String username = authService.getUsernameFromRedis(idx);
            if (username != null) {
                return ResponseEntity.ok(username);
            } else {
                return ResponseEntity.status(404).body("Username not found in Redis");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-username")
    public ResponseEntity<String> getUsernameFromHeader(@RequestHeader("X-User-Sub") String idx) {
        try {
            log.info("Header: {}", idx);
            String username = authService.getUsernameFromRedis(idx);
            if (username != null) {
                return ResponseEntity.ok(username);
            } else {
                return ResponseEntity.status(404).body("Username not found in Redis");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


     */

    @GetMapping("/auth/{id}")
    public ResponseEntity<RedisMember> getAuth(@PathVariable("id") String id){//아직도 get으로하면 405
        log.info("getAuth 요청옴!");
        Optional<RedisMember> found = redisMemberService.findById(id);
        if(found.isPresent()){
            return ResponseEntity.ok(found.get());
        }else {
            return ResponseEntity.status(401).build();
        }

    }
}
