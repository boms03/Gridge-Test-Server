package com.example.demo.src.board;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.image.ImageRepository;
import com.example.demo.src.board.entity.Board;
import com.example.demo.src.board.model.PostBoardReq;
import com.example.demo.src.board.model.PostBoardRes;
import com.example.demo.src.image.entity.Image;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.PostUserRes;
import com.google.cloud.storage.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

    private final ImageRepository imageRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final Bucket bucket;

    public PostBoardRes createBoard(List<MultipartFile> files, PostBoardReq postBoardReq, Long userId){

        if(files.isEmpty()){
            throw new BaseException(BaseResponseStatus.EMPTY_FILE);
        }

        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_USER));

        Board saveBoard = boardRepository.save(postBoardReq.toEntity(user));

        List<String> urlList = new ArrayList<>();

        files.forEach(it -> {
            String blob = user.getName() + "/" + user.getUsername() + "/" + user.getName()+ "_" + LocalDateTime.now()
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();

            urlList.add(blob);

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

        return new PostBoardRes(saveBoard.getId());
    }

    public void deletePost(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new BaseException(BaseResponseStatus.EMPTY_FILE));
        board.deleteBoard();
    }

}
