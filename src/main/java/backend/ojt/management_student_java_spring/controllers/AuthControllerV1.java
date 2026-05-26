package backend.ojt.management_student_java_spring.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestLogin;
import backend.ojt.management_student_java_spring.services.AuthService;
import backend.ojt.management_student_java_spring.utils.annotations.ApiMessage;
import backend.ojt.management_student_java_spring.utils.exceptions.BadDataException;
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

    @PostMapping("/register")
    @ApiMessage("Register account with student")
    public ResponseEntity<?> register(@Valid @RequestBody RequestLogin requestLogin) {
        if (requestLogin.getConfirmPassword().equals(requestLogin.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.register(requestLogin));
        }
        throw new BadDataException("Password and confirm password are not same thing");
    }
}
