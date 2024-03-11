package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /*
    @PostMapping("/posts")
    public Map post(@RequestBody @Valid PostCreate request) {
        // Case1. 저장한 데이터 Entity -> response 로 응답하기
        // Case2. 저장한 데이터의 primary_id -> response 로 응답하기
        // Case3. 응답 필요없음 -> 클라이언트에서 모든 Post(글) 데이터를 Context를 잘 관리함
        // BadCase => 서버에서 반드시 이렇게 할 것이라고 fix
        //            서버에서 차라리 유연하게 대응하는게 좋습니다.
        Long postId = postService.write(request);
        return Map.of("postId", postId);
    }
     */

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        postService.write(request);
    }

    /**
     * /post -> 글 전체 조회(검색 + 페이징)
     * /post/{postId} -> 글 1개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        // Request 클래스 (요청과 타당성 검증 클래스)
        // Response 클래스로 나누기 (서비스 정책에 맞는 로직이 들어가는 클래스)
        return postService.get(id);
    }

    // 조회 API
    // 게시글 여러개 조회
    @GetMapping("/posts")
    public List<PostResponse> getList() {
        return postService.getList();
    }


}
