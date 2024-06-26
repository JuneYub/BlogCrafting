package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.domain.PostEditor;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.exception.PostNotFound;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;

import com.blog.api.response.PostResponse;
import jakarta.transaction.Transactional;
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
                .orElseThrow(PostNotFound::new);

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

    public List<PostResponse> getList(PostSearch postSearch) {
//         Pageale 자체 설정 버전
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));

//        방법 1 Pageable 을 활용해 페이징 처리
//        return postRepository.findAll(pageable).stream()
//                .map(PostResponse::new)
//                .collect(Collectors.toList());

//        방법 2 QueryDsl 을 활용해서 페이징 처리 하는 방법
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
      
        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);
    }


    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
