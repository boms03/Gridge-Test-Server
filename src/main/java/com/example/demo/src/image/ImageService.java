package com.example.demo.src.image;

import com.example.demo.src.board.entity.Board;
import com.example.demo.src.image.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public List<String> getImageUrlList(Board board){
        List<Image> imageList = imageRepository.findAllByBoardOrderByCreatedAtAsc(board);
        List <String> imageUrlList =imageList.stream()
                .map(image -> image.getUrl())
                .collect(Collectors.toList());
        return imageUrlList;
    }
}
