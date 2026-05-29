package backend.ojt.management_student_java_spring.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ojt.management_student_java_spring.configs.CustomUserDetails;
import backend.ojt.management_student_java_spring.domain.dto.request.RequestAccount;
import backend.ojt.management_student_java_spring.domain.dto.request.RequestRegister;
import backend.ojt.management_student_java_spring.services.AuthService;
import backend.ojt.management_student_java_spring.utils.annotations.ApiMessage;
import backend.ojt.management_student_java_spring.utils.exceptions.RequestDataException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerV1 {
    final AuthService authService;
    final AuthenticationManagerBuilder builder;
    @Value("${djnd.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    /**
     * Register account for student
     * 
     * @param requestRegister
     * @return data user register
     **/
    @PostMapping("/register")
    @ApiMessage("Register account with student")
    public ResponseEntity<?> register(@Valid @RequestBody RequestRegister requestRegister) {
        if (requestRegister.getConfirmPassword().equals(requestRegister.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(requestRegister));
        }
        throw new RequestDataException("Password and confirm password are not same thing");
    }

    /**
     * Login with credential
     * usernamePasswordAuthenticationToken: [Create authentication request
     * Include (principal: email, credentials: password, authenticated: false)]
     * 
     * auth: [Check login
     * AuthenticatinoManager -> AuthenticationProvider ->
     * UserDetailCustoms -> loadUserByUsername]
     **/
    @PostMapping("/login")
    @ApiMessage("Login account")
    public ResponseEntity<?> login(@Valid @RequestBody RequestAccount requestAccount) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(requestAccount.getEmail(),
                requestAccount.getPassword());
        var auth = this.builder.getObject().authenticate(usernamePasswordAuthenticationToken);
        var principal = auth.getPrincipal();
        var userCustom = (CustomUserDetails) principal;
        var user = userCustom.user();
        var res = this.authService.generateLoginResponse(user);
        // browser send when server call api, client un set up
        ResponseCookie cookie = ResponseCookie.from("refresh_token", res.getRefreshToken())
                .httpOnly(true) // javaScript unread coookies
                .secure(true) // cookies only send by https
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(res);

    }
}
