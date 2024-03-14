package com.example.demo.src.board;

import com.example.demo.common.jwt.CustomUserDetail;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final JwtService jwtService;

    @PostMapping("")
    public BaseResponse<PostBoardRes> createBoard(
            @RequestPart(value="images", required= false) List<MultipartFile> files,
            @RequestPart(value = "requestDto") PostBoardReq postBoardReq,
            HttpServletRequest request
    ){
        Long id = jwtService.getUserId(request);
        PostBoardRes postBoardRes = boardService.createBoard(files,postBoardReq,id);
        return new BaseResponse<>(postBoardRes);
    }

    @DeleteMapping("")
    public BaseResponse<String> deleteBoard(
            @RequestParam Long boardId
    ){
        boardService.deletePost(boardId);
        String result = "게시물 삭제 완료";
        return new BaseResponse<>(result);
    }

}
