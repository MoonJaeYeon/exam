package org.koreait.exam.models.board;

import lombok.RequiredArgsConstructor;
import org.koreait.exam.controllers.board.BoardDataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveService {

    @Autowired
    private BoardSaveValidator validator;

    @Autowired
    private BoardDataDao boardDataDao;

    public void save(BoardDataForm data) {
        validator.check(data);

        boardDataDao.save(data);
    }
}