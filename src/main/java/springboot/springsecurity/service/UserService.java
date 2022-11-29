package springboot.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.springsecurity.domain.dto.UserDto;
import springboot.springsecurity.domain.dto.UserJoinRequest;
import springboot.springsecurity.domain.entity.User;
import springboot.springsecurity.exception.ErrorCode;
import springboot.springsecurity.exception.HospitalReviewException;
import springboot.springsecurity.repository.UserJpaRepository;
import springboot.springsecurity.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60;//시간

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

    public String login(String username, String password) {
        //username 확인
        User user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new HospitalReviewException(ErrorCode.NOT_FOUND, String.format("%s", "가입된 유저가 아닙니다")));

        //password 일치 확인
        if (!encoder.matches(password, user.getPassword())) {
            throw new HospitalReviewException(ErrorCode.INVALID_PASSWORD, String.format("%s", "아이디 또는 비밀번호가 일치하지 않습니다"));
        }

        //일치하면 로그인 토큰 반환
        return JwtUtil.createToken(username, secretKey, expireTimeMs);
    }
}
