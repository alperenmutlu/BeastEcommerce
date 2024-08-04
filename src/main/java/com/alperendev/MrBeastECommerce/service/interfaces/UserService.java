package com.alperendev.MrBeastECommerce.service.interfaces;

import com.alperendev.MrBeastECommerce.dto.LoginRequest;
import com.alperendev.MrBeastECommerce.dto.Response;
import com.alperendev.MrBeastECommerce.dto.UserDto;
import com.alperendev.MrBeastECommerce.entity.User;

public interface UserService {

    Response registerUser(UserDto registrationRequest);
    Response loginUser(LoginRequest loginRequest);
    Response getAllUsers();
    User getLoginUser();
    Response getUserInfoAndOrderHistory();
}
