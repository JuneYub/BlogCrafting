package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(requestPost);
        // 클라이언트 요구사항
        // json 응답에서 title 값을 최대 10글자로 해주세요 -> Post의 getTitle을 수정한다? -> 예를 들면 rss를 가져와야 하는 부분에서
        // 전체 제목을 가져와야 함에 불과하고 10글자부분만 잘라 가져가면 문제가 생길 수 있다.
        // 그렇기 때문에 서비스에 정책을 넣지 말자 => 응답 클래스를 분리하자

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("1234567890", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    /**
     * 페이징 처리 사용 이유
     * 1. 글이 너무 많으면 데이터가 내려갈 수 있다.
     * 2. 서버에서 글을 가져오는 트래픽 비용이 커진다.
     */
    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        // give
        List<Post> requestPosts = IntStream.range(1, 31)
                        .mapToObj(i -> {
                            return Post.builder()
                                    .title("글 제목 - " + i)
                                    .content("아브라카다브라 " + i)
                                    .build();
                        })
                        .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        // when
        List<PostResponse> posts = postService.getList(postSearch);
        // then
        assertEquals(10L, posts.size());
        assertEquals("글 제목 - 30", posts.get(0).getTitle());
        assertEquals("글 제목 - 21", posts.get(9).getTitle());
    }



    /* 페이징 처리가 있으므로 사용하지 않는다.
    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        // given
//        Post requestPost1 = Post.builder()
//                .title("foo1")
//                .content("bar1")
//                .build();
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
        ));

//        Post requestPost2 = Post.builder()
//                .title("foo2")
//                .content("bar2")
//                .build();
//        postRepository.save(requestPost2);

        // 클라이언트 요구사항
        // json 응답에서 title 값을 최대 10글자로 해주세요 -> Post의 getTitle을 수정한다? -> 예를 들면 rss를 가져와야 하는 부분에서
        // 전체 제목을 가져와야 함에 불과하고 10글자부분만 잘라 가져가면 문제가 생길 수 있다.
        // 그렇기 때문에 서비스에 정책을 넣지 말자 => 응답 클래스를 분리하자

        // when
        List<PostResponse> posts = postService.getList();

        // then
        assertEquals(2, postRepository.count());
    }
    */

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        // given
        Post post = Post.builder()
                .title("한남")
                .content("반포자이")
                .build();
        // when
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("더힐")
                .content("반포자이")
                .build();

        postService.edit(post.getId(), postEdit);

        // then
        Post changeedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        Assertions.assertEquals("더힐", changeedPost.getTitle());
        Assertions.assertEquals("반포자이", changeedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        // given
        Post post = Post.builder()
                .title("한남")
                .content("반포자이")
                .build();
        // when
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("한남")
                .content("초가집")
                .build();

        postService.edit(post.getId(), postEdit);

        // then
        Post changeedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        Assertions.assertEquals("한남", changeedPost.getTitle());
        Assertions.assertEquals("초가집", changeedPost.getContent());
    }
}