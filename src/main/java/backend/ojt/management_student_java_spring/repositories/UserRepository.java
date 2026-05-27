package backend.ojt.management_student_java_spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import backend.ojt.management_student_java_spring.domain.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmailIgnoreCase(String email);

    /**
     * get user by email when login with credential
     * 
     * @param email
     * @return optional user
     **/
    @Query(value = "select u from User u where lower(u.email) = lower(:email)")
    Optional<User> findWithDetailByEmail(@Param("email") String email);

    /**
     * update refresh token with user ID
     * 
     * @param userId
     * @param refreshToken
     * @return updated if updated <= 0 not found user
     **/
    @Modifying
    @Query(value = "update User u set u.refreshToken = :refreshToken where u.id = :userId")
    int updateRefreshTokenByUserId(@Param("userId") Long userId,
            @Param("refreshToken") String refreshToken);

}
