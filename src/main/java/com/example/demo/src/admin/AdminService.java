package com.example.demo.src.admin;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.BoardInfoRes;
import com.example.demo.src.admin.model.BoardReportInfoRes;
import com.example.demo.src.admin.model.UserInfoRes;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.board.repository.BoardRepository;
import com.example.demo.src.comment.CommentRepository;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.mapping.boardReport.entity.BoardReport;
import com.example.demo.src.mapping.boardReport.repository.BoardReportRepository;
import com.example.demo.src.subscription.entity.Subscription;
import com.example.demo.src.subscription.repository.SubscriptionRepository;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardReportRepository boardReportRepository;
    private final SubscriptionRepository subscriptionRepository;
    public Page<UserInfoRes> getUsers(Pageable pageable, String username, String name, String createdAt, Constant.UserState state){
        Page<User> userPage = userRepository.findUsersBySearchOption(pageable, username, name, createdAt, state);

        Page<UserInfoRes> userInfoReqs =  userPage.map(user ->
                    UserInfoRes.builder()
                        .id(user.getId())
                        .phoneNumber(user.getUsername())
                        .email(user.getEmail())
                        .isOAuth(user.isOAuth())
                        .role(user.getRole().toString())
                        .provider(user.getProvider())
                        .state(user.getState().toString())
                        .username(user.getUsername())
                        .name(user.getName())
                        .lastAgreedAt(user.getLastAgreedAt())
                        .build()
                );

        return  userInfoReqs;
    }

    public void banUser(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));

        if(user.getState() == Constant.UserState.BANNED){
            throw new BaseException(BaseResponseStatus.BANNED_USER);
        }

        user.setState(Constant.UserState.BANNED);
    }
    public Page<BoardInfoRes> getBoards(Pageable pageable, String username, String createdAt, Constant.BoardState state){
        Page<Board> boardPage = boardRepository.findBoardsBySearchOption(pageable, username, createdAt, state);

        Page<BoardInfoRes> boardInfoRes =  boardPage.map(board ->
                BoardInfoRes.builder()
                        .id(board.getId())
                        .content(board.getContent())
                        .location(board.getLocation())
                        .state(board.getState().toString())
                        .boardState(board.getBoardState().toString())
                        .build()
        );

        return  boardInfoRes;
    }

    public void deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        if(board.getState() == Constant.State.INACTIVE){
            throw new BaseException(BaseResponseStatus.DELETED_BOARD);
        }

        List<Comment> commentList = commentRepository.findAllByBoardAndState(board, Constant.State.ACTIVE);

        commentList.forEach(Comment::deleteComment);

        board.deleteBoard();
    }

    public Page<UserInfoRes> getSubscriptions(Pageable pageable, String username, String name, String createdAt, String endAt, Constant.SubscriptionState state){

        Page<Subscription> subscriptionPage = subscriptionRepository.findSubscriptionsBySearchOption(pageable,username,name,createdAt,endAt,state);

        Page<UserInfoRes> userInfoResPage =  subscriptionPage.map(subscription ->
                UserInfoRes.builder()
                        .id(subscription.getUser().getId())
                        .phoneNumber(subscription.getUser().getUsername())
                        .email(subscription.getUser().getEmail())
                        .isOAuth(subscription.getUser().isOAuth())
                        .role(subscription.getUser().getRole().toString())
                        .provider(subscription.getUser().getProvider())
                        .state(subscription.getUser().getState().toString())
                        .username(subscription.getUser().getUsername())
                        .name(subscription.getUser().getName())
                        .lastAgreedAt(subscription.getUser().getLastAgreedAt())
                        .build()
        );

        return userInfoResPage;
    }

    public Page<BoardReportInfoRes> getBoardReports(Pageable pageable){
        Page<BoardReport> boardReportPage = boardReportRepository.findBoardReportsBySearchOption(pageable);

        Page<BoardReportInfoRes> boardReportInfoResPage =  boardReportPage.map(boardReport ->
                BoardReportInfoRes.builder()
                        .id(boardReport.getId())
                        .reportContent(boardReport.getReport().getTitle())
                        .boardId(boardReport.getBoard().getId())
                        .userId(boardReport.getUser().getId())
                        .state(boardReport.getState().toString())
                        .build()
        );

        return boardReportInfoResPage;

    }

    public void deleteBoardReport(Long boardReportId){
        BoardReport boardReport = boardReportRepository.findById(boardReportId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD_REPORT));

        if(boardReport.getState() == Constant.State.INACTIVE){
            throw new BaseException(BaseResponseStatus.DELETED_BOARD_REPORT);
        }

        boardReport.setState(Constant.State.INACTIVE);

    }
}
