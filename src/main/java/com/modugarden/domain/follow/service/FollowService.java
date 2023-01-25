package com.modugarden.domain.follow.service;

import com.modugarden.common.error.enums.ErrorMessage;
import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.common.response.BaseResponseDto;
import com.modugarden.domain.follow.dto.FollowResponseDto;
import com.modugarden.domain.follow.dto.isFollowedResponseDto;
import com.modugarden.domain.follow.entity.Follow;
import com.modugarden.domain.follow.repository.FollowRepository;
import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
//여기서는 인자에 @ 사용 안함

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public BaseResponseDto<isFollowedResponseDto> follow(User user, Long id) {
        // user가 아닌 dto를 써줘야 함
        // 원래는 UserService말고 객체가 와야 함
        // User fromUser = userDetail.getUser();
        Optional<User> oToUser = userRepository.findById(id);
        oToUser.orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND)); //예외처리
        Long fromUser = user.getId();
        User toUser = oToUser.get();

        //access를 private에서 public으로 바꿈
        //setter을 쓰는 게 안 좋음
        Follow follow = new Follow(user, toUser);
        followRepository.save(follow);

        return new BaseResponseDto(new BaseResponseDto<>(ErrorMessage.SUCCESS));
    }

    @Transactional
    //변화가 필요할 때 transactional 사용
    public BaseResponseDto<isFollowedResponseDto> unFollow(User user, Long id) {
        // 원래는 UserService말고 객체가 와야 함
        // User fromUser = userDetail.getUser();
        Optional<User> oToUser = userRepository.findById(id);
        oToUser.orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));  //예외처리
        //User 대신 user 객체가 와야 함
        Long fromUser = user.getId();
        User toUser = oToUser.get();

        followRepository.deleteByFollowingUserAndUser(user.getId(), oToUser.get().getId());
        //리턴을 dto로 해야 한다.
        return new BaseResponseDto(new BaseResponseDto<>(ErrorMessage.SUCCESS));
    }


    //팔로우 유무 체크
    public int profile(Long id, User user) {

        Optional<User> oToUser = userRepository.findById(id);
        oToUser.orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_FOUND));  //예외처리
        //User 대신 user 객체가 와야 함

        int followcheck = followRepository.countByUserAndFollowingUser(user.getId(), oToUser.get().getId());
        return followcheck;
    }

    //user가 following user을 following 함
    //following user을 user가 follower 함
    //pathvariable 부분만 넣고 postman에서 잘 돌아가는지 확인하기
    public Slice<FollowResponseDto> followerList(Long id, User user, Pageable pageable) {
        Slice<Follow> followerList = followRepository.findByFollowingUser(id, pageable);
        Slice<Follow> pricipalFollowerLists = followRepository.findByFollowingUser(user.getId(), pageable);

        for (Follow f1 : followerList) {
            for (Follow f2 : pricipalFollowerLists) {
                if (f1.getFollowingUser().getId() == f2.getFollowingUser().getId()) {
                    f1.setMatpal(true);
                }
            }
        }
        // list대신에 slice 형식으로 리턴
        return followerList(id, user, pageable);
    }

    public Slice<FollowResponseDto> followingList(Long id, User user, Pageable pageable) {
        Slice<Follow> followingList = followRepository.findByUser(id, pageable);  //팔로워 리스트
        Slice<Follow> pricipalFollowingLists = followRepository.findByFollowingUser(user.getId(), pageable);  //팔로우 리스트

        for (Follow f1 : followingList) {
            for (Follow f2 : pricipalFollowingLists) {
                if (f1.getUser().getId() == f2.getFollowingUser().getId()) {
                    f1.setMatpal(true);
                }
            }
        }
        return followingList(id, user, pageable);
    }

    public Slice<Follow> findByFollowingId(Long id, Pageable pageable) {
        return followRepository.findByUser(id, pageable);
    }
}