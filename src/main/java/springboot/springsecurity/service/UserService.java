package springboot.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.springsecurity.domain.UserDto;
import springboot.springsecurity.domain.UserJoinRequest;
import springboot.springsecurity.domain.entity.User;
import springboot.springsecurity.exception.ErrorCode;
import springboot.springsecurity.exception.HospitalReviewException;
import springboot.springsecurity.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder encoder;

    public UserDto join(UserJoinRequest request) {
        //비즈니스 로직 - 회원가입

        //회원 username(id) 중복 체크
        //중복이면 회원가입 x -> exception(예외)발생
        //orElseThrow는 없는 경우
        userJpaRepository.findByUsername(request.getUsername())
                .ifPresent(user -> {
                    throw new HospitalReviewException(ErrorCode.DUPLICATED_USER_NAME, String.format("username: %s", request.getUsername()));
                });

        //회원가입 save()
        String passwordEncode = encoder.encode(request.getPassword());
        User saveUser = userJpaRepository.save(request.toEntity(passwordEncode));

        return UserDto.builder()
                .id(saveUser.getId())
                .username(saveUser.getUsername())
                .emailAddress(saveUser.getEmailAddress())
                .build();
    }

}
