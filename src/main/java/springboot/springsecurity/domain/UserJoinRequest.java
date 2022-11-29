package springboot.springsecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.springsecurity.domain.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

public class UserJoinRequest {

    private String username;
    private String password;
    private String emailAddress;

    public User toEntity(String password) {
        return User.builder()
                .username(this.username)
                .password(password)
                .emailAddress(this.emailAddress)
                .build();
    }


}
