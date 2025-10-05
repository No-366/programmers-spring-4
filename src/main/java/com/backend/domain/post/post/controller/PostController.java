package com.backend.domain.post.post.controller;

import com.backend.domain.post.post.entity.Post;
import com.backend.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private String getWriteFormHtml(String errorMessage){
        return """
                <div style="color:red">%s</div>
                <form method="POST" action="http://localhost:8080/posts/doWrite">
                  <input type="text" name="title">
                  <br>
                  <textarea name="content"></textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                """.formatted(errorMessage);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write(){

        return getWriteFormHtml("");
    }

    //POST 요청으로 받는다
    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            String title,
            String content
    ){

        if(title.isBlank()) return getWriteFormHtml("제목을 입력해주세요");
        if(content.isBlank()) return getWriteFormHtml("내용을 입력해주세요");

        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}
