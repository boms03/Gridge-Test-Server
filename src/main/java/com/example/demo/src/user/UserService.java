package com.example.demo.src.user;



import com.example.demo.common.Constant.*;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.mapping.userAgree.UserAgreeRepository;
import com.example.demo.src.mapping.userAgree.entity.UserAgree;
import com.example.demo.src.terms.TermsRepository;
import com.example.demo.src.terms.entity.Terms;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAgreeRepository userAgreeRepository;
    private final TermsRepository termsRepository;
    private final JwtService jwtService;


    //POST
    public PostUserRes createUser(PostUserReq postUserReq) {
        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserReq.toEntity());
        List<Terms> termsList = termsRepository.findAll();

        termsList.forEach(termsEntity -> {
                    UserAgree userAgree = UserAgree.builder()
                        .user(saveUser)
                        .terms(termsEntity).build();
                    userAgreeRepository.save(userAgree);
                });


        return new PostUserRes(saveUser.getId());

    }

    public PostUserRes createOAuthUser(User user) {
        User saveUser = userRepository.save(user);

        // JWT 발급
        String jwtToken = jwtService.createJwt(saveUser.getId(), String.valueOf(saveUser.getRole()));
        return new PostUserRes(saveUser.getId(), jwtToken);

    }

    public void modifyUserName(Long userId, PatchUserReq patchUserReq) {
        User user = userRepository.findByIdAndState(userId, UserState.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.updateName(patchUserReq.getName());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, UserState.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(UserState.ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    @Transactional(readOnly = true)
    public List<GetUserRes> getUsersByEmail(String email) {
        List<GetUserRes> getUserResList = userRepository.findAllByEmailAndState(email, UserState.ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }


    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userId) {
        User user = userRepository.findByIdAndState(userId, UserState.ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    @Transactional(readOnly = true)
    public boolean checkUserByEmail(String email) {
        Optional<User> result = userRepository.findByEmailAndState(email, UserState.ACTIVE);
        if (result.isPresent()) return true;
        return false;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        User user = userRepository.findByUsername(postLoginReq.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        // 유저 상태 유효성 검사
        if(user.getState() == UserState.BANNED){
            throw new BaseException(BANNED_USER);
        } else if (user.getState() == UserState.WITHDRAW){
            throw new BaseException(WITHDRAW_USER);
        } else if (user.getState() == UserState.RENEW){
            throw new BaseException(RENEW_USER);
        }

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            Long userId = user.getId();
            String jwt = jwtService.createJwt(userId, String.valueOf(user.getRole()));
            return new PostLoginRes(userId,jwt);
        } else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public GetUserRes getUserByEmail(String email) {
        User user = userRepository.findByEmailAndState(email, UserState.ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    public boolean checkUserByPhoneNumber(String phoneNumber) {
        Optional<User> checkUser = userRepository.findByPhoneNumber(phoneNumber);
        return checkUser.isPresent();
    }

    public boolean checkUserByUserName(String username) {
        Optional<User> checkUser = userRepository.findByUsername(username);
        return checkUser.isPresent();
    }
}
