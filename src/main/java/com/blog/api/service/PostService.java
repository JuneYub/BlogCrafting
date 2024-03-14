package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        // 포스트 서비스에서 조회해온 포스트를 포스트 리스폰스로 변환하는 작업을 하는게 맞나?
        /**
         * Controller -> WebPostService( 리스폰스 관련 ) => Repository
         *               PostService (외부와 통신 관련 ) 할 수도 있지만 그냥 하나에 퉁 치기도 함
         *
         *               중요한 점은 응답을 위한 클래스를 별도로 분리했다는 점이다.
         */

    }

    /*
    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
//                .map(post -> {
//                    return PostResponse.builder()
//                            .id(post.getId())
//                            .title(post.getTitle())
//                            .content(post.getContent())
//                            .build();
//                })
                 // 위 방식은 반복적인 작업을 많이 한다. 생성자로 퉁 쳐줄 수 있다
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }
    */

    public List<PostResponse> getList(Pageable pageable) {
        //Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }




}
