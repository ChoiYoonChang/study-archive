package study.jwt.controller;

import study.jwt.dto.LoginRequest;
import study.jwt.dto.TokenResponse;
import study.jwt.security.JwtTokenProvider;
import study.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request){

        if(userService.validateUser(request.getUsername(), request.getPassword())){
            String token = jwtTokenProvider.generateToken(request.getUsername());
            return ResponseEntity.ok(new TokenResponse(token));
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
