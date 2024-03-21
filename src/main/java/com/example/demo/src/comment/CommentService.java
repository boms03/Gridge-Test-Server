package com.example.demo.src.comment;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.board.repository.BoardRepository;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.comment.entity.Comment;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetCommentsRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.Time;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final int pageSize = 10;
    public void createComment(PostCommentReq postCommentReq, Long userId){
        Board board = boardRepository.findByIdAndState(postCommentReq.getBoardId(), Constant.State.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));
        User user = userRepository.findByIdAndState(userId, Constant.UserState.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));


        commentRepository.save(new Comment(board,user,postCommentReq.getContent()));
    }

    public GetCommentRes getComment(Long commentId){

        Comment comment = commentRepository.findByIdAndState(commentId,Constant.State.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        return buildGetCommentRes(comment);
    }

    public List<GetCommentRes> getCommentsOfBoard(Board board){
        List<Comment> commentList = commentRepository.findAllByBoardAndState(board, Constant.State.ACTIVE);

        return commentList.stream()
                        .map(this::buildGetCommentRes)
                        .collect(Collectors.toList());
    }

    public GetCommentsRes fetchCommentPagesBy(Long boardId, Long lastCommentId) {

        Board board = boardRepository.findByIdAndState(boardId, Constant.State.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        Page<Comment> comments = fetchPages(board, lastCommentId, pageSize);

        List<GetCommentRes> commentsList = comments.stream()
                .map(this::buildGetCommentRes)
                .collect(Collectors.toList());

        if(commentsList.isEmpty()){
            return new GetCommentsRes(commentsList);
        }

        Long newLastCommentId = commentsList.get(commentsList.size()-1).getCommentId();

        return new GetCommentsRes(commentsList, newLastCommentId);

    }

    public Page<Comment> fetchPages(Board board, Long lastCommentId, int size) {

        PageRequest pageRequest = PageRequest.of(0, size);

        if (lastCommentId == -1) {
            return commentRepository.findTop10ByStateAndBoardOrderByIdDesc(Constant.State.ACTIVE, board, pageRequest);
        } else {
            return commentRepository.findByIdLessThanAndStateAndBoardOrderByIdDesc(lastCommentId, Constant.State.ACTIVE, board, pageRequest);
        }
    }

    public GetCommentRes buildGetCommentRes(Comment comment){

        String formattedTime = Time.formatPostTime(comment);

        return new GetCommentRes(comment, formattedTime);
    }

    public Long countCommentsOfBoard(Board board){
        return commentRepository.countAllByBoardAndState(board, Constant.State.ACTIVE);
    }

    public void deleteComment(Long commentId, Long userId){
        Comment comment = commentRepository.findByIdAndState(commentId, Constant.State.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_COMMENT));
        if(!Objects.equals(comment.getBoard().getUser().getId(), userId)){
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }
        comment.deleteComment();
    }

}
