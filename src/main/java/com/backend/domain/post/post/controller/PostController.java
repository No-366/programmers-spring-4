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

    private String getWriteFormHtml(String errorMessage, String title, String content, String errorFieldName){
        return """
                <div style="color:red">%s</div>
                <form method="POST" action="http://localhost:8080/posts/doWrite">
                  <input type="text" name="title" value="%s">
                  <br>
                  <textarea name="content" >%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                
                <script>
                    const errorFieldName = "%s";
                    if(errorFieldName.length > 0) {
                        const form = document.querySelector("form");
                        form[errorFieldName].focus();
                    }
                </script>
                """.formatted(errorMessage, title, content, errorFieldName);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write(){

        return getWriteFormHtml("", "","", "title");
    }

    //POST 요청으로 받는다
    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            String title,
            String content
    ){

        if(title.isBlank()) return getWriteFormHtml("제목을 입력해주세요", title, content,"title");
        if(title.length()<2) return getWriteFormHtml("제목은 2글자 이상 적어주세요.", title, content,"title");
        if(title.length()>10) return getWriteFormHtml("제목은 10글자 이하로 적어주세요", title, content,"title");
        if(content.isBlank()) return getWriteFormHtml("내용을 입력해주세요", title, content, "content");
        if(content.length()<2) return getWriteFormHtml("내용은 2글자 이상 적어주세요.", title, content,"content");
        if(content.length()>100) return getWriteFormHtml("내용은 100글자를 넘을 수 없습니다", title, content,"content");

        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}
