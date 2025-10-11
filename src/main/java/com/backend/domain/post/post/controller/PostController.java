package com.backend.domain.post.post.controller;

import com.backend.domain.post.post.entity.Post;
import com.backend.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



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


    @GetMapping("/posts/write")
    public String write(){
        return "post/write"; // post는 template하위의 write.html이 들어있는 폴더명
    }

    //POST 요청으로 받는다
    @PostMapping("/posts/doWrite")
    public String doWrite(
            //다음은 프레임워크 규칙임
            //@ModelAttribute("")은 생략 가능 , 객체들은 자동으로 model에 담긴다는듯
            //@ModelAttribute를 이용해서 이름 재정의 안할 시 : PostWriteform 클래스이름의 첫번째 단어를 소문자로 바꿔서 매개값으로 사용
            @Valid @ModelAttribute("postWriteForm") PostWriteForm form // PostWriteForm 클래스 형태로 변환해서 받겠다
            , BindingResult bindingResult//보고서 : 이걸 도입하면 내부적으로 체크하되 문제가 발생해도 일단 넘어감
            , Model model
    ){
        //보고서 사용법
        if(bindingResult.hasErrors()){

            //ex)4 - 내용은 2글자 이상
            // => content - 4 - 내용은 2글자 이상
            // => <!--4--><li data-error-field-name = "content">내용은 2글자 이상</li>
            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(field -> field.getField() + "-" + field.getDefaultMessage())
                    .map(message -> message.split("-"))
                    .map(bits -> """
                            <!-- %s --><li data-error-field-name = "%s">%s</li>
                            """.formatted(bits[1],bits[0], bits[2]))
                    .sorted()
                    .collect(Collectors.joining(""));

            model.addAttribute("errorMessages", errorMessages); // model을 통해 데이터를 넘겨준다
            return "post/write";
        }

        // 아래처럼 일일이 여기에 작성하지 말고 NotBlank와 Size, Validated를 이용하자
//        if(title.isBlank()) return getWriteFormHtml("제목을 입력해주세요", title, content,"title");
//        if(title.length()<2) return getWriteFormHtml("제목은 2글자 이상 적어주세요.", title, content,"title");
//        if(title.length()>10) return getWriteFormHtml("제목은 10글자 이하로 적어주세요", title, content,"title");
//        if(content.isBlank()) return getWriteFormHtml("내용을 입력해주세요", title, content, "content");
//        if(content.length()<2) return getWriteFormHtml("내용은 2글자 이상 적어주세요.", title, content,"content");
//        if(content.length()>100) return getWriteFormHtml("내용은 100글자를 넘을 수 없습니다", title, content,"content");

        Post post = postService.write(form.title, form.content);

        model.addAttribute("id", post.getId());
        //model.addAttribute("form", form);
        // 굳이 새로 넘겨주지 않아도 위에서 @ModelAttribute에 의해 "postWriteForm"이라는 이름으로 이미 사용가능하고,
        // 바로 위에서의 model.addAttribute..로 인해 넘겨진다
        return "post/writeDone";
    }
}
