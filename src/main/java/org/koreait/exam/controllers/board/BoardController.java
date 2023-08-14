package org.koreait.exam.controllers.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.koreait.exam.models.board.BoardData;
import org.koreait.exam.models.board.BoardDataDao;
import org.koreait.exam.models.board.InfoService;
import org.koreait.exam.models.board.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final SaveService saveService;
    private final InfoService infoService;
    private final BoardDataDao boardDataDao;
    @GetMapping("/write")
    public String write(@ModelAttribute BoardDataForm data) {

        return "board/write";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute BoardDataForm data) {

        return "board/list";
    }

    @PostMapping("/save")
    public String save(@Valid BoardDataForm data, Errors errors) {
        // 에러 발생 시 write 이동
        if (errors.hasErrors()) {
            return "board/write";
        }

        saveService.save(data);

        return "redirect:/board/view/" + data.getId();
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable long id, Model model) {

        BoardData data = infoService.get(id);

        model.addAttribute("data", data);

        return "board/view";
    }

    @ExceptionHandler(RuntimeException.class)
    public String errorHandler(RuntimeException e, Model model) {

        String script = String.format("alert('%s');history.back();", e.getMessage());
        model.addAttribute("script", script);

        e.printStackTrace();

        return "commons/execute_script";
    }
}
