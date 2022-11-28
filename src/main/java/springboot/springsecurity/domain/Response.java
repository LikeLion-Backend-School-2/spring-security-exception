package springboot.springsecurity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    //resultcode : 무슨 에러가 났는지 에러코드
    //result : 성공을 반환할 때 T로 감싸서 리턴
    //모든 response는 이 response 객체로 감싸서 return
    //joinResponse, loginResponse ...  dto처럼 감싸서 보내겠다는 것

    //static으로 생성

    private String resultCode;
    private T result;

    public static Response<Void> error(String resultCode) {
        return new Response(resultCode, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response("SUCCESS", result);
    }

}
