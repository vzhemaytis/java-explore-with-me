package ru.ewm.service.user.mapper;

import ru.ewm.service.user.dto.UserDto;
import ru.ewm.service.user.dto.UserShortDto;
import ru.ewm.service.user.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static User toUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
