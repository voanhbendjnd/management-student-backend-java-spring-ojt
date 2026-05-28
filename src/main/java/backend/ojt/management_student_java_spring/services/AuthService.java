package backend.ojt.management_student_java_spring.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.ojt.management_student_java_spring.domain.dto.request.RequestRegister;
import backend.ojt.management_student_java_spring.domain.dto.res.ResLogin;
import backend.ojt.management_student_java_spring.domain.entity.User;
import backend.ojt.management_student_java_spring.repositories.UserRepository;
import backend.ojt.management_student_java_spring.utils.SecurityUtils;
import backend.ojt.management_student_java_spring.utils.constains.UserRole;
import backend.ojt.management_student_java_spring.utils.constains.UserStatus;
import backend.ojt.management_student_java_spring.utils.exceptions.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final SessionManager sessionManager;
    final SecurityUtils securityUtils;
    final StudentService studentService;
    @Value("${djnd.jwt.access-token-validity-in-seconds}")
    private Long expiresIn;

    /**
     * Student create account
     * 
     * @param request
     * @return
     **/
    @Transactional
    public long register(RequestRegister request) {
        var user = this.userRepository.save(User.builder()
                .email(request.getEmail().toLowerCase())
                .gender(request.getGender())
                .name(request.getName())
                .password(this.passwordEncoder.encode(request.getConfirmPassword()))
                .phone(request.getPhone())
                .status(UserStatus.ACTIVE)
                .role(UserRole.STUDENT)
                .build());
        this.studentService.createStudent(user);
        return user.getId();

    }

    /*
     * Return reponse information account and manage token
     */
    @Transactional
    public ResLogin generateLoginResponse(User user) {
        var res = new ResLogin();
        var userLogin = new ResLogin.UserLogin();
        var email = user.getEmail();

        userLogin.setEmail(email);
        userLogin.setId(user.getId());
        userLogin.setName(user.getName());
        userLogin.setPhone(user.getPhone());
        userLogin.setRole(user.getRole().toString());

        res.setUser(userLogin);
        var sessionID = this.sessionManager.createNewSession(user);
        var accessToken = this.securityUtils.createAccessToken(email, res, sessionID, user.getRole());
        var refreshToken = this.securityUtils.createRefreshToken(email, res);
        var updated = this.userRepository.updateRefreshTokenByUserId(user.getId(), refreshToken);
        if (updated <= 0) {
            throw new NotFoundException("User not found!");
        }
        res.setAccessToken(accessToken);
        res.setExpiresIn(expiresIn);
        res.setRefreshToken(refreshToken);
        return res;

    }

}
