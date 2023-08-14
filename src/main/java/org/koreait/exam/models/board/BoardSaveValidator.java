package org.koreait.exam.models.board;

import org.koreait.exam.controllers.board.BoardDataForm;
import org.koreait.exam.validators.RequiredValidator;
import org.koreait.exam.validators.Validator;
import org.springframework.stereotype.Component;

@Component
public class BoardSaveValidator implements Validator<BoardDataForm>, RequiredValidator {

    @Override
    public void check(BoardDataForm data) {
        checkRequired(data.getPoster(), new BoardValidationException("작성자를 입력하세요."));
        checkRequired(data.getSubject(), new BoardValidationException("제목을 입력하세요."));
        checkRequired(data.getContent(), new BoardValidationException("내용을 입력하세요."));

    }
}
