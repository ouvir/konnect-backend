package com.konnect.user.service;

import com.konnect.user.dto.UserDTO;

public interface UserService {
    UserDTO findById(long id);
}
