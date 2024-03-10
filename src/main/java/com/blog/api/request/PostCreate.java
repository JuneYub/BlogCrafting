package com.blog.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;
    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 빌더의 장점
    // 1. 가독성에 좋다. (값 생성에 유리함)
    // 2. 필요한 값만 받을 수 있다. // 오버로딩 int x,y 를 필드값을 가지는 클래스가 있는데
    // 생성자로 int x,y 1개 int a,b 개 를 두면 컴파일 에러가 난다. (오버로딩이 안되는 조건)
    // 이를 빌더패턴으로 해결할 수 있다.
    // 3. 객체의 불변성(중요) setter 안써도 됨
}
