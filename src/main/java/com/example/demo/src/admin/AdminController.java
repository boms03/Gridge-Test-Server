package com.example.demo.src.admin;

import com.example.demo.common.Constant;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.BoardInfoRes;
import com.example.demo.src.admin.model.BoardReportInfoRes;
import com.example.demo.src.admin.model.UserInfoRes;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.mapping.boardReport.entity.BoardReport;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public BaseResponse<Page<UserInfoRes>> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String createdAt,
            @RequestParam(required = false) Constant.UserState state,
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getUsers(pageable,username,name,createdAt,state));
    }

    @DeleteMapping("/user/ban")
    public BaseResponse<String> banUser(
            @RequestParam Long id
    ){
        adminService.banUser(id);
        String response = "계정 정지 완료";
        return new BaseResponse<>(response);
    }

    @GetMapping("/boards")
    public BaseResponse<Page<BoardInfoRes>> getBoards(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String createdAt,
            @RequestParam(required = false) Constant.BoardState state,
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getBoards(pageable,username,createdAt,state));
    }

    @DeleteMapping("/board")
    public BaseResponse<String> deleteBoard(
            @RequestParam Long boardId
    ){
        adminService.deleteBoard(boardId);
        String response = "게시글 삭제 완료";
        return new BaseResponse<>(response);
    }

    @GetMapping("/subscriptions")
    public BaseResponse<Page<UserInfoRes>> getSubscriptions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String createdAt,
            @RequestParam(required = false) String endAt,
            @RequestParam(required = false) Constant.SubscriptionState state,
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getSubscriptions(pageable,username,name,createdAt,endAt,state));
    }

    @GetMapping("/boardReports")
    public BaseResponse<Page<BoardReportInfoRes>> getBoardReports(
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getBoardReports(pageable));
    }

    @DeleteMapping("/boardReport")
    public BaseResponse<String> deleteBoardReport(
            @RequestParam Long boardReportId
    ){
        adminService.deleteBoardReport(boardReportId);
        String response = "게시글 삭제 완료";
        return new BaseResponse<>(response);
    }

}
