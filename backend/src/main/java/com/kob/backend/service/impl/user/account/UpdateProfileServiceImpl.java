package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.UpdateProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UpdateProfileServiceImpl implements UpdateProfileService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String emailAddress = data.get("emailAddress");
        String username = data.get("username");

        Map<String, String> map = new HashMap<>();

        if (emailAddress == null || emailAddress.length() == 0) {
            map.put("error_message", "Email cannot empty");
            return map;
        }

        if (username == null) {
            map.put("error_message", "Username cannot empty");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "Username cannot empty");
            return map;
        }

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(userQueryWrapper);
        if (!users.isEmpty() && users.get(0).getUsername() == username) {
            map.put("error_message", "Username Exists");
            return map;
        }

        QueryWrapper<User> emailQueryWrapper = new QueryWrapper<User>();
        emailQueryWrapper.eq("email_address", emailAddress);
        List<User> emails = userMapper.selectList(emailQueryWrapper );
        if (!emails.isEmpty() && emails.get(0).getEmailAddress() == emailAddress) {
            map.put("error_message", "Email Exists");
            return map;
        }

        User new_user = new User(user.getId(),
                username,
                user.getPassword(),
                user.getPhoto(),
                user.getFirstName(),
                user.getLastName(),
                emailAddress);

        userMapper.updateById(new_user);

        map.put("error_message", "success");

        return map;
    }
}
