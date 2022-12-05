package springboot.springsecurity.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.springsecurity.domain.entity.User;
import springboot.springsecurity.service.UserService;
import springboot.springsecurity.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {//입장할 때 마다(요청할 떄마다) 매번 불특정한 사람들이 요청 티켓을가지고 체크

    private final String secretKey;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //권한을 주거나 주지 않는다 ( 입장 ex) 티켓 확인)
        //개찰구 역할
        //현재는 모두 닫혀 있습니다

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader:{}", authorizationHeader);

        //토큰만 분리해야한다
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token;
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {//요청시 token이 없으면
            log.error("token 추출에 실패 했습니다");
            filterChain.doFilter(request, response);
            return;
        }

        //JWTTokenUtil.
        if (JwtUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Token에서 userName 꺼내기
        String userName = JwtUtil.getUserName(token, secretKey);
        log.info("username:{}", userName);

        //userDEtail 가져오기
        User user = userService.getUserByUserName(userName);
        log.info("userRole:{}", user.getRole());

        //문 열어주기, role 바인딩
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, List.of(new SimpleGrantedAuthority(user.getRole().name())));//권한을 여러개 줄 수 있다   );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);//권한 부여 (이 문을 통과할 수 있다는 것)
        filterChain.doFilter(request, response);

    }
}
