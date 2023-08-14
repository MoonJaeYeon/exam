package org.koreait.exam.models.board;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoardData {
    private long id;
    private String poster;
    private String subject;
    private String content;
    private LocalDateTime regDt;
    private LocalDateTime modDt;
}
