package com.konnect.service;

import com.konnect.dto.UserDTO;

public interface UserService {
    UserDTO findById(long id);
}
