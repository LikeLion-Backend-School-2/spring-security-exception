package springboot.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.springsecurity.domain.Response;
import springboot.springsecurity.domain.UserDto;
import springboot.springsecurity.domain.UserJoinRequest;
import springboot.springsecurity.domain.UserJoinResponse;
import springboot.springsecurity.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    //1. response 추상화 : 에러코드를 포함시켜 리턴하기 위함
    //타입을 넘겨줘야함
    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getUsername(), userDto.getEmailAddress()));
    }

}
