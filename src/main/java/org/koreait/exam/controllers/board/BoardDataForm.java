package org.koreait.exam.controllers.board;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDataForm {
    private long id;

    @NotBlank(message="제목을 입력하세요.")
    private String subject;

    @NotBlank(message="작성자를 입력하세요.")
    private String poster;

    @NotBlank(message="내용을 입력하세요.")
    private String content;
}