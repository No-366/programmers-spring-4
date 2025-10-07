package com.backend.domain.post.post.controller;

import com.backend.domain.post.post.entity.Post;
import com.backend.domain.post.post.service.PostService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

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

    //입력값을 모아서 객체로 사용하기위한 역할
    @AllArgsConstructor
    @Getter
    public static class PostWriteForm{
        @NotBlank(message = "1-제목을 입력해주세요")
        @Size(min = 1, max = 10, message = "2-제목은 2글자 이상, 10글자 이하로 입력해주세요")
        private String title;

        @NotBlank(message = "3-내용을 입력해주세요")
        @Size(min = 1, max = 100, message = "4-내용은 2글자 이상 100글자 이하로 입력해주세요")
        private String content;
    }


    //POST 요청으로 받는다
    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            //다음은 프레임워크 규칙임
            //@ModelAttribute("")은 생략 가능,
            //PostWriteform 클래스이름의 첫번째 단어를 소문자로 바꿔서 매개값으로 사용
        @Validated @ModelAttribute("postWriteForm") PostWriteForm form // PostWriteForm 클래스 형태로 변환해서 받겠다
            , BindingResult bindingResult//보고서 : 이걸 도입하면 내부적으로 체크하되 문제가 발생해도 일단 넘어감
    ){
        //보고서 사용법
        if(bindingResult.hasErrors()){

            String fieldName = "title";

            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .sorted()
                    .collect(Collectors.joining("<br>"));

            return getWriteFormHtml(errorMessages, form.title, form.content, fieldName);
        }

        // 아래처럼 일일이 여기에 작성하지 말고 NotBlank와 Size, Validated를 이용하자
//        if(title.isBlank()) return getWriteFormHtml("제목을 입력해주세요", title, content,"title");
//        if(title.length()<2) return getWriteFormHtml("제목은 2글자 이상 적어주세요.", title, content,"title");
//        if(title.length()>10) return getWriteFormHtml("제목은 10글자 이하로 적어주세요", title, content,"title");
//        if(content.isBlank()) return getWriteFormHtml("내용을 입력해주세요", title, content, "content");
//        if(content.length()<2) return getWriteFormHtml("내용은 2글자 이상 적어주세요.", title, content,"content");
//        if(content.length()>100) return getWriteFormHtml("내용은 100글자를 넘을 수 없습니다", title, content,"content");

        Post post = postService.write(form.title, form.content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}
