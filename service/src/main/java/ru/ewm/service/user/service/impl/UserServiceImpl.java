package ru.ewm.service.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.user.dto.UserDto;
import ru.ewm.service.user.model.User;
import ru.ewm.service.user.repository.UserRepository;
import ru.ewm.service.user.service.UserService;

import static ru.ewm.service.user.mapper.UserMapper.toUser;
import static ru.ewm.service.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto addUser(UserDto userDto) {
        User userToSave = toUser(userDto);
        User savedUser = userRepository.save(userToSave);
        return toUserDto(savedUser);
    }
}
