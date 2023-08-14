package org.koreait.exam.models.board;

public class BoardDataNotFoundException extends RuntimeException {
    public BoardDataNotFoundException() {
        super("등록된 게시글이 아닙니다.");
    }
}
