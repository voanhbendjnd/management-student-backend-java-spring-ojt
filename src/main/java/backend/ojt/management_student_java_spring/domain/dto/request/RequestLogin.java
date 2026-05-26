package backend.ojt.management_student_java_spring.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import backend.ojt.management_student_java_spring.utils.constains.UserGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLogin {
    Long id;
    @NotBlank(message = "Name empty!")
    String name;
    @Email(message = "Email invalid format!")
    @NotBlank(message = "Email nempty!")
    String email;
    String phone;
    @NotNull(message = "Gender not empty!")
    UserGender gender;
    @Size(min = 6, message = "The password has fewer than 6 ccharacters!")
    String password;
    @Size(min = 6, message = "The confirm password has fewer than 6 characters!")
    @NotNull(message = "Password empty!")
    @JsonProperty(value = "confirm_password")
    @NotNull(message = "Confirm password empty!")

    String confirmPassword;

}
