package com.backend.domain.post.post.controller;

import com.backend.domain.post.post.entity.Post;
import com.backend.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/write")
    @ResponseBody
    public String write(){

        return """
                <form action="http://localhost:8080/posts/doWrite">
                  <input type="text" name="title">
                  <br>
                  <textarea name="content"></textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                """;
    }

    @GetMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            String title,
            String content
    ){
        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}
