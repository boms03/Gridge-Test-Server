package com.example.demo.src.admin;

import com.example.demo.common.Constant;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.BoardInfoRes;
import com.example.demo.src.admin.model.BoardReportInfoRes;
import com.example.demo.src.admin.model.UserInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Operation(summary = "Get 모든 유저 조회")
    @ApiResponse(responseCode = "200", description = "모든 유저 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 유저 조회 실패")
    @GetMapping("/users")
    public BaseResponse<Page<UserInfoRes>> getUsers(
            @Parameter(required = true, name = "유저 아이디")
            @RequestParam(required = false) String username,

            @Parameter(required = true, name = "유저 이름")
            @RequestParam(required = false) String name,

            @Parameter(required = true, name = "유저 생성 시간")
            @RequestParam(required = false) String createdAt,

            @Parameter(required = true, name = "유저 상태")
            @RequestParam(required = false) Constant.UserState state,

            @Parameter(required = true, description = "페이지, 사이즈, 정렬 기준")
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getUsers(pageable,username,name,createdAt,state));
    }

    @Operation(summary = "Put 유저 정지")
    @ApiResponse(responseCode = "200", description = "유저 정지 성공")
    @ApiResponse(responseCode = "400", description = "유저 정지 실패")
    @PutMapping("/user/ban")
    public BaseResponse<String> banUser(
            @Parameter(required = true, name = "유저 아이디")
            @RequestParam Long id
    ){
        adminService.banUser(id);
        String response = "계정 정지 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Get 모든 게시물 조회")
    @ApiResponse(responseCode = "200", description = "모든 게시물 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 게시물 조회 실패")
    @GetMapping("/boards")
    public BaseResponse<Page<BoardInfoRes>> getBoards(
            @Parameter(required = true, name = "유저 아이디")
            @RequestParam(required = false) String username,

            @Parameter(required = true, name = "게시글 생성 시간")
            @RequestParam(required = false) String createdAt,

            @Parameter(required = true, name = "게시글 상태")
            @RequestParam(required = false) Constant.BoardState state,

            @Parameter(required = true, description = "페이지, 사이즈, 정렬 기준")
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getBoards(pageable,username,createdAt,state));
    }

    @Operation(summary = "Put 게시물 삭제")
    @ApiResponse(responseCode = "200", description = "게시물 삭제 성공")
    @ApiResponse(responseCode = "400", description = "게시물 삭제 실패")
    @PutMapping("/board")
    public BaseResponse<String> deleteBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @RequestParam Long boardId
    ){
        adminService.deleteBoard(boardId);
        String response = "게시글 삭제 완료";
        return new BaseResponse<>(response);
    }

    @Operation(summary = "Get 모든 구독 조회")
    @ApiResponse(responseCode = "200", description = "모든 구독 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 구독 조회 실패")
    @GetMapping("/subscriptions")
    public BaseResponse<Page<UserInfoRes>> getSubscriptions(
            @Parameter(required = true, name = "유저 아이디")
            @RequestParam(required = false) String username,

            @Parameter(required = true, name = "유저 이름")
            @RequestParam(required = false) String name,

            @Parameter(required = true, name = "구독 생성 시간")
            @RequestParam(required = false) String createdAt,

            @Parameter(required = true, name = "구독 종료 시간")
            @RequestParam(required = false) String endAt,

            @Parameter(required = true, name = "구독 상태")
            @RequestParam(required = false) Constant.SubscriptionState state,

            @Parameter(required = true, description = "페이지, 사이즈, 정렬 기준")
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getSubscriptions(pageable,username,name,createdAt,endAt,state));
    }

    @Operation(summary = "Get 모든 게시물 신고 조회")
    @ApiResponse(responseCode = "200", description = "모든 게시물 신고 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 게시물 신고 조회 실패")
    @GetMapping("/boardReports")
    public BaseResponse<Page<BoardReportInfoRes>> getBoardReports(
            @Parameter(required = true, description = "페이지, 사이즈, 정렬 기준")
            @PageableDefault(sort = "createdAt")
            Pageable pageable
    ){
        return new BaseResponse<>(adminService.getBoardReports(pageable));
    }

    @Operation(summary = "Put 게시물 신고 삭제")
    @ApiResponse(responseCode = "200", description = "게시물 신고 삭제 성공")
    @ApiResponse(responseCode = "400", description = "게시물 신고 삭제 실패")
    @PutMapping("/boardReport")
    public BaseResponse<String> deleteBoardReport(
            @Parameter(required = true, name = "게시물 신고 아이디")
            @RequestParam Long boardReportId
    ){
        adminService.deleteBoardReport(boardReportId);
        String response = "게시글 신고 삭제 완료";
        return new BaseResponse<>(response);
    }

}
