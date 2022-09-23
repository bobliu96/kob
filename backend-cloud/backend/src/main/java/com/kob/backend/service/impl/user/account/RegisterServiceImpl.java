package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username,
                                        String password,
                                        String confirmedPassword,
                                        String firstName,
                                        String lastName,
                                        String emailAddress,
                                        Integer win,
                                        Integer lose,
                                        Integer draw) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "Username cannot empty");
            return map;
        }
        if (password == null || confirmedPassword == null) {
            map.put("error_message", "Password cannot empty");
            return map;
        }
        if (firstName == null || firstName.length() == 0) {
            map.put("error_message", "First name cannot empty");
            return map;
        }
        if (lastName == null || lastName.length() == 0) {
            map.put("error_message", "Last name cannot empty");
            return map;
        }
        if (emailAddress == null || emailAddress.length() == 0) {
            map.put("error_message", "Email Address cannot empty");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "Username cannot empty");
            return map;
        }
        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "Password cannot empty");
            return map;
        }
        if (username.length() > 100) {
            map.put("error_message", "Username cannot greater than 100");
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "Password cannot greater than 100");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "Password does not match");
            return map;
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(userQueryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "Username Exists");
            return map;
        }

        QueryWrapper<User> emailQueryWrapper = new QueryWrapper<User>();
        emailQueryWrapper.eq("email_address", emailAddress);
        List<User> emails = userMapper.selectList(emailQueryWrapper );
        if (!emails.isEmpty()) {
            map.put("error_message", "Email Exists");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/4579_lg_b261836c72.jpg";
        User user = new User(null, username, encodedPassword, photo, firstName, lastName, emailAddress, 1500, 0, 0, 0);
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;
    }
}