package com.ll.tem.domain.post.post.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {
    private String getFormHtml(String errorMessage, String title, String content) {
        return """
                <div>%s</div>
                <form method="POST">
                    <input type="text" name="title" placeholder="제목" value="%s">
                    <br>
                    <textarea name="content" placeholder="내용">%s</textarea>
                    <br>
                    <button type="submit">글쓰기</button>
                </form>
                """.formatted(errorMessage, title, content);
    }

    @GetMapping
    @ResponseBody
    public String showList() {
        String body = """
                <h1>글 목록</h1>
                
                <ul>
                    <li>글 3</li>
                    <li>글 2</li>
                    <li>글 1</li>
                </ul>
                
                <a href="/posts/write">글쓰기</a>
                """;

        return body;
    }

    @GetMapping("/write")
    @ResponseBody
    public String showWrite() {
        return getFormHtml("", "", "");
    }

    private record PostWriteForm(
            @NotBlank(message = "01-제목을 입력해주세요.")
            @Length(min = 5, message = "02-제목을 5자 이상 입력해주세요.")
            String title,
            @NotBlank(message = "03-내용을 입력해주세요.")
            @Length(min = 10, message = "04-내용을 10자 이상 입력해주세요.")
            String content
    ) {
    }

    @PostMapping("/write")
    @ResponseBody
    public String write(
            @Valid PostWriteForm form,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .sorted()
                    .map(message -> message.split("-", 2)[1])
                    .collect(Collectors.joining("<br>"));

            return getFormHtml(
                    errorMessages,
                    form.title,
                    form.content
            );
        }

        return """
                <h1>글쓰기 완료</h1>
                
                <div>
                    <h2>%s</h2>
                    <p>%s</p>
                </div>
                """.formatted(form.title, form.content);
    }
}