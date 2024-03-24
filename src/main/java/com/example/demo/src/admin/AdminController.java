package com.example.demo.src.admin;

import com.example.demo.common.Constant;
import com.example.demo.common.response.BaseErrorResponse;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.BoardInfoRes;
import com.example.demo.src.admin.model.BoardReportInfoRes;
import com.example.demo.src.admin.model.UserInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 어드민 모든 유저 조회 API
     * [GET] /admin/users
     * @param username : 유저 아이디, nullable
     * @param name : 유저 이름, nullable
     * @param createdAt : 유저 생성 시간, nullable
     * @param state : 유저 상태, nullable
     * @param page : 페이지, nullable
     * @param size : 한 페이지에 불러올 크기, nullable
     * @return BaseResponse<Page<UserInfoRes>>
     */
    @Operation(summary = "Get 모든 유저 조회")
    @ApiResponse(responseCode = "200", description = "모든 유저 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 유저 조회 실패" , content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/users")
    public Page<UserInfoRes> getUsers(
            @Parameter(name = "유저 아이디")
            @RequestParam(required = false) String username,

            @Parameter(name = "유저 이름")
            @RequestParam(required = false) String name,

            @Parameter(name = "유저 생성 시간")
            @RequestParam(required = false) String createdAt,

            @Parameter(name = "유저 상태")
            @RequestParam(required = false) Constant.UserState state,

            @Parameter(name = "페이지")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(name = "한 페이지에 불러올 크기")
            @RequestParam(name = "size", defaultValue = "10") int size

    ){
        Pageable pageable = PageRequest.of(page,size);
        Page<UserInfoRes> userInfoResPage = adminService.getUsers(pageable,username,name,createdAt,state);
        return userInfoResPage;
    }

    /**
     * 어드민 유저 정지 API
     * [PUT] /admin/users/{userId}
     * @param userId: 정지할 유저 아이디
     * @return BaseResponse<String>
     */
    @Operation(summary = "Put 유저 정지")
    @ApiResponse(responseCode = "200", description = "유저 정지 성공")
    @ApiResponse(responseCode = "400", description = "유저 정지 실패")
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PutMapping("/users/{userId}")
    public BaseResponse<String> banUser(
            @Parameter(required = true, name = "정지할 유저 아이디")
            @PathVariable Long userId
    ){
        adminService.banUser(userId);
        String response = "계정 정지 완료";
        return new BaseResponse<>(response);
    }

    /**
     * 어드민 모든 유저 조회 API
     * [GET] /admin/users
     * @param username : 작성자 아이디, nullable
     * @param createdAt : 게시물 생성 시간, nullable
     * @param state : 게시물 상태, nullable
     * @param page : 페이지, nullable
     * @param size : 한 페이지에 불러올 크기, nullable
     * @return BaseResponse<Page<UserInfoRes>>
     */
    @Operation(summary = "Get 모든 게시물 조회")
    @ApiResponse(responseCode = "200", description = "모든 게시물 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 게시물 조회 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/boards")
    public BaseResponse<Page<BoardInfoRes>> getBoards(
            @Parameter(name = "유저 아이디")
            @RequestParam(required = false) String username,

            @Parameter(name = "게시글 생성 시간")
            @RequestParam(required = false) String createdAt,

            @Parameter(name = "게시글 상태")
            @RequestParam(required = false) Constant.BoardState state,

            @Parameter(name = "페이지")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(name = "한 페이지에 불러올 크기")
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return new BaseResponse<>(adminService.getBoards(pageable,username,createdAt,state));
    }

    /**
     * 어드민 게시물 삭제 API
     * [PUT] /admin/board/{boardId}
     * @param boardId: 삭제할 게시물 아이디
     * @return BaseResponse<String>
     */
    @Operation(summary = "Put 게시물 삭제")
    @ApiResponse(responseCode = "200", description = "게시물 삭제 성공")
    @ApiResponse(responseCode = "400", description = "게시물 삭제 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PutMapping("/boards/{boardId}")
    public BaseResponse<String> deleteBoard(
            @Parameter(required = true, name = "게시글 아이디")
            @PathVariable Long boardId
    ){
        adminService.deleteBoard(boardId);
        String response = "게시글 삭제 완료";
        return new BaseResponse<>(response);
    }


    /**
     * 어드민 모든 구독 조회 API
     * [GET] /admin/subscriptions
     * @param username : 구독자 아이디, nullable
     * @param name : 구독자 이름, nullable
     * @param createdAt : 구독 생성 시간, nullable
     * @param state : 구독 상태, nullable
     * @param page : 페이지 , nullable
     * @param size : 한 페이지에 불러올 크기, nullable
     * @return BaseResponse<Page<UserInfoRes>>
     */
    @Operation(summary = "Get 모든 구독 조회")
    @ApiResponse(responseCode = "200", description = "모든 구독 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 구독 조회 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/subscriptions")
    public BaseResponse<Page<UserInfoRes>> getSubscriptions(
            @Parameter(name = "유저 아이디")
            @RequestParam(required = false) String username,

            @Parameter(name = "유저 이름")
            @RequestParam(required = false) String name,

            @Parameter(name = "구독 생성 시간")
            @RequestParam(required = false) String createdAt,

            @Parameter(name = "구독 종료 시간")
            @RequestParam(required = false) String endAt,

            @Parameter(name = "구독 상태")
            @RequestParam(required = false) Constant.SubscriptionState state,

            @Parameter(name = "페이지")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(name = "한 페이지에 불러올 크기")
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return new BaseResponse<>(adminService.getSubscriptions(pageable,username,name,createdAt,endAt,state));
    }

    /**
     * 어드민 모든 게시글 신고 조회 API
     * [GET] /admin/boardReports
     * @param page : 페이지 , nullable
     * @param size : 한 페이지에 불러올 크기, nullable
     * @return BaseResponse<Page<UserInfoRes>>
     */
    @Operation(summary = "Get 모든 게시물 신고 조회")
    @ApiResponse(responseCode = "200", description = "모든 게시물 신고 조회 성공")
    @ApiResponse(responseCode = "400", description = "모든 게시물 신고 조회 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @GetMapping("/boardReports")
    public BaseResponse<Page<BoardReportInfoRes>> getBoardReports(
            @Parameter(name = "페이지")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Parameter(name = "한 페이지에 불러올 크기")
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return new BaseResponse<>(adminService.getBoardReports(pageable));
    }

    /**
     * 어드민 받은 게시글 신고 삭제 API
     * [PUT] /admin/boardReports/{boardReportId}
     * @param boardReportId: 삭제할 게시글 신고 아이디
     * @return BaseResponse<String>
     */
    @Operation(summary = "Put 게시물 신고 삭제")
    @ApiResponse(responseCode = "200", description = "게시물 신고 삭제 성공")
    @ApiResponse(responseCode = "400", description = "게시물 신고 삭제 실패", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @ApiResponse(responseCode = "500", description = "서버 에러", content = {@Content(schema = @Schema(implementation = BaseErrorResponse.class))} )
    @PutMapping("/boardReports/{boardReportId}")
    public BaseResponse<String> deleteBoardReport(
            @Parameter(required = true, name = "게시물 신고 아이디")
            @PathVariable Long boardReportId
    ){
        adminService.deleteBoardReport(boardReportId);
        String response = "게시글 신고 삭제 완료";
        return new BaseResponse<>(response);
    }

}
