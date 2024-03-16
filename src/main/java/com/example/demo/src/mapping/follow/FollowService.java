package com.example.demo.src.mapping.follow;

import com.example.demo.common.Constant;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.mapping.follow.entity.Follow;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.PostFollowReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void createFollow(String followeeUsername, Long id){
        User follower = userRepository.findByIdAndState(id, Constant.UserState.ACTIVE).orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));
        User followee = userRepository.findByUsernameAndState(followeeUsername, Constant.UserState.ACTIVE).orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));

        Follow follow = Follow.builder()
                .followee(followee)
                .follower(follower)
                .build();

        Follow saveFollow = followRepository.save(follow);
    }

    public void deleteFollow(String followeeUsername, Long id){
        User followee = userRepository.findByUsernameAndState(followeeUsername, Constant.UserState.ACTIVE)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_USER));

        Follow follow = followRepository.findByFollowerIdAndFolloweeId(id, followee.getId())
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_FOLLOW));

        followRepository.delete(follow);
    }

    public List<User> findFollowings(Long id){
        List<Follow> followingList = followRepository.findAllByFollowerId(id);

        List<User> followingIds = followingList.stream()
                .map(follow -> follow.getFollowee())
                .filter(followee -> followee.getState() == Constant.UserState.ACTIVE)
                .collect(Collectors.toList());

        return followingIds;
    }
}
