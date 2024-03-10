package com.blog.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Builder
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public String getTitle() {
        return this.title.substring(0, 10);
    }
}
