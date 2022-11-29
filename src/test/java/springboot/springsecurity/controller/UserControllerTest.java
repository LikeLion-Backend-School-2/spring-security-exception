package springboot.springsecurity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import springboot.springsecurity.domain.dto.UserDto;
import springboot.springsecurity.domain.dto.UserJoinRequest;
import springboot.springsecurity.exception.ErrorCode;
import springboot.springsecurity.exception.HospitalReviewException;
import springboot.springsecurity.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser//가상의 유효한 user 설정
@WebMvcTest
@DisplayName("TODO")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    //user
    UserJoinRequest userJoinRequest = UserJoinRequest.builder()
            .username("seoyun")
            .password("1234")
            .emailAddress("seo@gmail.com")
            .build();

    @DisplayName("회원가입 성공")
    @Test
    void join_success() throws Exception {


        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("회원가입 실패")
    @Test
    void join_fail() throws Exception {

        when(userService.join(any())).thenThrow(new HospitalReviewException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("로그인실패 - 아이디 없음")
    @Test
    void login_fail_id() throws Exception {
        //무엇을 보내서 : id, pw
        //무엇을 받을까 : not_found
        String id = "seoyun";
        String password = "2345";

        when(userService.login(any(), any())).thenThrow(new HospitalReviewException(ErrorCode.NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @DisplayName("로그인실패 - 비밀번호 일치하지 않음")
    @Test
    void login_fail_password() {

    }

    @DisplayName("로그인 성공")
    @Test
    void login_success() {

    }
}