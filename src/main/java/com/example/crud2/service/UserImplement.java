package com.example.crud2.service;

import com.example.crud2.entity.UserEntity;
import com.example.crud2.model.GetResp;
import com.example.crud2.model.PostModel;
import com.example.crud2.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserImplement implements UserService{

    private final UserRepository userRepository;

    public UserImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetResp findAll(int page, int per_page, Sort.Direction orderBy, String sortBy) {
        Sort sort = Sort.by(orderBy, sortBy);
        PageRequest pageRequest = PageRequest.of(page-1, per_page, sort);
        Page<UserEntity> result = userRepository.findByDeletedFalse(pageRequest);
        return new GetResp(result.getTotalElements(), page, per_page, result.getContent());
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        try {
            Optional<UserEntity> existingUser =  userRepository.findById(id);
            if (existingUser.isPresent())
                return ResponseEntity.ok(existingUser);
            else
                throw new NoSuchElementException("User with ID " + id + " not found");

        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> saveUser(UserEntity userEntity) {
        try {
            UserEntity user =  userRepository.save(userEntity);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new PostModel(user.getId(), "User has been created"));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> updateUser(Long id, UserEntity userEntity) {
        try {
            Optional<UserEntity> user = userRepository.findById(id);
            if (user.isPresent()) {
                UserEntity existingUser = user.get();
                userRepository.save(existingUser);
                return ResponseEntity
                        .ok(Collections
                                .singletonMap("message", "User with id " + id + " has been updated"));
            } else {
                throw new NoSuchElementException("User with id " + id + " not found");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        }

    }

    public ResponseEntity<?> patchUser(Long id, UserEntity userEntity) {
        try {
            Optional<UserEntity> user = userRepository.findById(id);
            if (user.isPresent()) {
                UserEntity existingUser = user.get();
                if (userEntity.getDeleted() != null) {
                    existingUser.setDeleted(userEntity.getDeleted());
                }
                userRepository.save(existingUser);
                return ResponseEntity
                        .ok(Collections
                                .singletonMap("message", "User with id " + id + " has been updated"));
            } else {
                throw new NoSuchElementException("User with id " + id + " not found");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            Optional<UserEntity> existingUser =  userRepository.findById(id);
            if (existingUser.isPresent()) {
                UserEntity user = existingUser.get();
                user.setDeleted(true);
                userRepository.save(user);
                return ResponseEntity.ok(Collections
                        .singletonMap("message", "User with ID " + id + " has been deleted"));
            }
            else
                throw new NoSuchElementException("User with ID " + id + " not found");
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections
                            .singletonMap("message", e.getMessage()));
        }
    }
}
