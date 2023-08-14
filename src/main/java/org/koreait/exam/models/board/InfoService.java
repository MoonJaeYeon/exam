package org.koreait.exam.models.board;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoService {
    private final BoardDataDao boardDataDao;

    public BoardData get(long id) {

        // 조회된 게시글이 없을 때
        BoardData data = boardDataDao.get(id);
        if (data == null) {
            throw new BoardDataNotFoundException();
        }

        return data;
    }
}