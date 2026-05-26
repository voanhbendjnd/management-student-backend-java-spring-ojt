package backend.ojt.management_student_java_spring.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestLogin;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.UserRepository;
import backend.ojt.management_student_java_spring.utils.constains.UserRole;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public long register(RequestLogin request) {
        var user = this.userRepository.save(User.builder()
                .email(request.getEmail())
                .gender(request.getGender())
                .name(request.getName())
                .password(this.passwordEncoder.encode(request.getConfirmPassword()))
                .phone(request.getPhone())
                .status(UserStatus.ACTIVE)
                .role(UserRole.STUDENT)
                .build());
        return user.getId();

    }
}
