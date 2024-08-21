package com.example.demo.common;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.demo.domain.User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }
        // カスタムのUserDetailsオブジェクトを作成して返す
        return new CustomUserDetails(
                user.getId(), // ID
                user.getUsername(), // 名前(メールアドレス)
                user.getPassword(), // パスワード
                // 権限情報を設定
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }
}
