package org.koreait.exam.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.koreait.exam.controllers.board.BoardDataForm;
import org.koreait.exam.models.board.BoardData;
import org.koreait.exam.models.board.BoardValidationException;
import org.koreait.exam.models.board.InfoService;
import org.koreait.exam.models.board.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("게시글 저장 서비스 테스트")
@Transactional // 테스트시에는 추가된 데이터를 다시 롤백
public class BoardSaveServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SaveService saveService;

    @Autowired
    private InfoService infoService;

    private BoardDataForm boardData;

    @BeforeEach
    void init() {
        boardData = getData();
    }

    private BoardDataForm getData() {
        BoardDataForm boardData = new BoardDataForm();
        boardData.setPoster("작성자");
        boardData.setSubject("제목");
        boardData.setContent("내용");

        return boardData;
    }

    @Test
    @DisplayName("게시글 작성,수정 성공시 예외가 발생하지 않음")
    void saveSuccessTest() {
        assertDoesNotThrow(() -> {
            saveService.save(boardData);
        });
    }

    @Test
    @DisplayName("필수 항목 검증, 검증 실패 BoardValidationException 발생")
    void requiredFieldsTest() {

        assertAll(
                () -> {
                    boardData = getData();
                    boardData.setPoster(null);
                    requiredFieldTestEach(boardData, "작성자");
                },
                () -> {
                    boardData = getData();
                    boardData.setPoster("     ");
                    requiredFieldTestEach(boardData, "작성자");
                },
                () -> {
                    boardData = getData();
                    boardData.setSubject(null);
                    requiredFieldTestEach(boardData, "제목");
                },
                () -> {
                    boardData = getData();
                    boardData.setSubject("     ");
                    requiredFieldTestEach(boardData, "제목");
                },
                () -> {
                    boardData = getData();
                    boardData.setContent(null);
                    requiredFieldTestEach(boardData, "내용");
                },
                () -> {
                    boardData = getData();
                    boardData.setContent("    ");
                    requiredFieldTestEach(boardData, "내용");
                }
        );
    }

    @Test
    @DisplayName("저장전 데이터와 저장된 데이터의 일치 여부")
    void saveResultTest() {
        saveService.save(boardData);
        BoardData result = infoService.get(boardData.getId());

        assertAll(
                () -> assertEquals(boardData.getPoster(), result.getPoster()),
                () -> assertEquals(boardData.getSubject(), result.getSubject()),
                () -> assertEquals(boardData.getContent(), result.getContent())
        );
    }

    private void requiredFieldTestEach(BoardDataForm data, String message) {
        BoardValidationException thrown = assertThrows(BoardValidationException.class, () -> {
            saveService.save(data);
        });
        assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    @DisplayName("성공시 게시글 보기 페이지 이동")
    void saveSuccessControllerTest() throws Exception {
        mockMvc.perform(post("/board/save")
                        .param("poster", boardData.getPoster())
                        .param("subject", boardData.getSubject())
                        .param("content", boardData.getContent()))
                .andExpect(status().is(302))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("필수 항목 검증, 작성자, 제목, 내용 포함 여부")
    void requiredFieldControllerTest() throws Exception {
        String body = mockMvc.perform(post("/board/save"))
                .andDo(print())
                .andReturn().getResponse().getContentAsString(); // Body 데이터

        assertAll(
                ()-> assertTrue(body.contains("작성자를 입력")),
                ()-> assertTrue(body.contains("제목을 입력")),
                ()-> assertTrue(body.contains("내용을 입력"))
        );

    }
}