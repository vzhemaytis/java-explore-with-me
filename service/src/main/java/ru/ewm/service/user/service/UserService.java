package ru.ewm.service.user.service;

import ru.ewm.service.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    void deleteUser(Long userId);

    List<UserDto> getUsers(List<Long> ids);

    List<UserDto> getAllUsers(long from, int size);
}
