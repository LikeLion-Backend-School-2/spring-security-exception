package springboot.springsecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.springsecurity.domain.dto.ReviewCreateRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @PostMapping("")
    public String write(@RequestBody ReviewCreateRequest reviewCreateRequest, Authentication authentication) {
        log.info("{}", authentication.getName());
        return "리뷰 등록에 성공했습니다";
    }

}
