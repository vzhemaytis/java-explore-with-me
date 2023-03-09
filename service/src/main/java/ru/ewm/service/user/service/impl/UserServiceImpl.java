package ru.ewm.service.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ewm.service.error.EntityNotFoundException;
import ru.ewm.service.user.dto.NewUserRequest;
import ru.ewm.service.user.dto.UserDto;
import ru.ewm.service.user.mapper.UserMapper;
import ru.ewm.service.user.model.User;
import ru.ewm.service.user.repository.UserRepository;
import ru.ewm.service.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.ewm.service.user.mapper.UserMapper.toUser;
import static ru.ewm.service.user.mapper.UserMapper.toUserDto;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(NewUserRequest userDto) {
        User userToSave = toUser(userDto);
        User savedUser = userRepository.save(userToSave);
        return toUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(userId, User.class.getSimpleName());
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids) {
        List<User> foundUsers = userRepository.findAllById(ids);
        return foundUsers.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUsers(long from, int size) {
        PageRequest pageSize = PageRequest.of(0, size);
        List<User> foundUsers = userRepository.findAllByIdIsGreaterThanEqual(from, pageSize);
        return foundUsers.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
