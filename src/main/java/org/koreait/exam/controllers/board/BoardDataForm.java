package org.koreait.exam.controllers.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class BoardDataForm {
    private long id;

    @NotBlank(message="제목을 입력하세요.")
    private String subject;

    @NotBlank(message="작성자를 입력하세요.")
    private String poster;

    @NotBlank(message="내용을 입력하세요.")
    private String content;
}