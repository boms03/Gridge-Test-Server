package com.example.demo.src.board;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.board.repository.BoardRepository;
import com.example.demo.src.mapping.boardLike.entity.BoardLike;
import com.example.demo.src.board.model.*;
import com.example.demo.src.comment.CommentService;
import com.example.demo.src.comment.model.GetCommentsRes;
import com.example.demo.src.image.ImageRepository;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.image.ImageService;
import com.example.demo.src.image.entity.Image;
import com.example.demo.src.mapping.boardLike.BoardLikeRepository;
import com.example.demo.src.mapping.follow.FollowService;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.Time;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final UserRepository userRepository;
    private final Bucket bucket;
    private final ImageService imageService;
    private final CommentService commentService;
    private final FollowService followService;
    private final int pageSize = 10;

    public void createBoard(List<MultipartFile> files, PostBoardReq postBoardReq, Long userId){

        if(files.isEmpty()){
            throw new BaseException(BaseResponseStatus.EMPTY_FILE);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        Board saveBoard = boardRepository.save(postBoardReq.toEntity(user));

        files.forEach(it -> {
            String blob = user.getName() + "/" + user.getUsername() + "/" + user.getName()+ "_" + LocalDateTime.now()
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();

            try {
                // 이미 존재하면 파일 삭제
                if(bucket.get(blob) != null) {
                    bucket.get(blob).delete();
                }
                // 파일을 Bucket에 저장
                bucket.create(blob, it.getBytes());

                Image image = Image.builder()
                        .board(saveBoard)
                        .url(blob)
                        .build();

                imageRepository.save(image);

            } catch (IOException e) {
                throw new BaseException(BaseResponseStatus.UPLOAD_FAILED);
            }
        });

    }

    public void deletePost(Long boardId, Long userId){

        Board board = boardRepository.findById(boardId).orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        if(!Objects.equals(board.getUser().getId(), userId)){
            throw new BaseException(BaseResponseStatus.ACCESS_DENIED);
        }

        board.deleteBoard();
    }

    public GetBoardRes getBoard(Long boardId,Long userId){

        Board board = boardRepository.findByIdAndState(boardId,Constant.State.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        if(board.getUser().getState() == Constant.UserState.WITHDRAW) {
            throw new BaseException(BaseResponseStatus.WITHDRAW_USER);
        } else if (board.getUser().getState() == Constant.UserState.BANNED){
            throw new BaseException(BaseResponseStatus.BANNED_USER);
        }

        return buildGetBoardRes(board,userId);
    }

    public GetBoardsRes fetchBoardPagesBy(Long lastBoardId, Long userId) {

        User user = userRepository.findByIdAndState(userId, Constant.UserState.ACTIVE)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        List<User> followers = followService.findFollowings(userId);
        // 자기 자신의 글도 피드에 올라옴
        followers.add(user);

        Page<Board> boards = fetchPages(lastBoardId, pageSize, followers);
        List<GetBoardRes> boardsList = boards.stream()
                .map(board -> buildGetBoardRes(board,userId))
                .collect(Collectors.toList());

        if(boardsList.isEmpty()){
            return new GetBoardsRes(boardsList);
        }

        Long newLastBoardId = boardsList.get(boardsList.size()-1).getBoardId();

        return new GetBoardsRes(boardsList, newLastBoardId);

    }

    public Page<Board> fetchPages(Long lastBoardId, int size, List<User> followers) {

        PageRequest pageRequest = PageRequest.of(0, size);

        if (lastBoardId == -1) {
            return boardRepository.findTop10ByStateAndUserInOrderByIdDesc(Constant.State.ACTIVE, followers, pageRequest);
        } else {
            return boardRepository.findByIdLessThanAndStateAndUserInOrderByIdDesc(lastBoardId, Constant.State.ACTIVE, followers, pageRequest);
        }
    }

    public void likeBoard(Long userId, Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        User user = userRepository.findById(userId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));

        boardLikeRepository.save(new BoardLike(board,user));
    }

    public void unlikeBoard(Long userId, Long boardId){
        BoardLike boardLike = boardLikeRepository.findByBoardIdAndUserId(boardId,userId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_BOARD));

        boardLikeRepository.delete(boardLike);
    }

    public Long countBoardLike(Board board){
        return boardLikeRepository.countAllByBoard(board);
    }

    public String getPreviewContent(Board board){
        String previewContent = board.getContent();

        if(board.getContent().length() + board.getUser().getUsername().length() >=100) {
            previewContent = board.getContent().substring(0,99-board.getUser().getUsername().length());
        }

        return previewContent;
    }

    public boolean isShortened(String content, String previewContent){
        return !previewContent.equals(content);
    }

    public boolean isLiked(Long boardId, Long userId){
        return boardLikeRepository.findByBoardIdAndUserId(boardId,userId).isPresent();
    }

    public GetBoardRes buildGetBoardRes(Board board, Long userId){

        List <String> imageUrlList = imageService.getImageUrlList(board);

        Long likes = countBoardLike(board);

        String formattedTime = Time.formatPostTime(board);

        String previewContent = getPreviewContent(board);

        boolean isShortened = isShortened(board.getContent(),previewContent);

        GetCommentsRes getCommentRes = commentService.fetchCommentPagesBy(board.getId(),-1L);

        Long countComment = commentService.countCommentsOfBoard(board);

        boolean isLiked = isLiked(board.getId(),userId);

        return new GetBoardRes(board, previewContent, isShortened, likes, isLiked, imageUrlList, formattedTime, getCommentRes, countComment);
    }

}
