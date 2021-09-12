package com.example.marimo_back.service;

import com.example.marimo_back.domain.UserDto;
import com.example.marimo_back.domain.Users;
import com.example.marimo_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto saveUser(Map<Object, String> userinfo) {
        String email = userinfo.get("email");
        String username = userinfo.get("username");

        List<Users> userList = userRepository.findUser(email);
        if (userList.size() > 0) {
            // 이미 저장된 유저라면 저장된 정보 넘겨주기
            Users user;
            try {
                user = userList.get(0);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
            return UserDto.builder().id(user.getId()).email(user.getEmail())
                    .nickname(user.getNickname()).character(user.getCharacter()).playnow(user.getPlaynow())
                    .playnum(user.getPlaynum()).premium(user.getPremium()).achieve(user.getAchieve()).build();
        }
        // 새로 가입하는 유저라면 정보 저장하기
        Users newUser = Users.builder().email(email).username(username).playnum(0).premium(false).build();
        userRepository.save(newUser);
        return UserDto.builder().id(newUser.getId()).email(newUser.getEmail())
                .nickname(newUser.getNickname()).character(newUser.getCharacter()).playnow(newUser.getPlaynow())
                .playnum(newUser.getPlaynum()).premium(newUser.getPremium()).achieve(newUser.getAchieve()).build();
    }

    public void saveName(Map<Object, String> userinfo) {
        Long id = Long.parseLong(userinfo.get("id"));
        String nickname = userinfo.get("nickname");

        Users user = userRepository.findById(id);

        user.setNickname(nickname);
    }
}
