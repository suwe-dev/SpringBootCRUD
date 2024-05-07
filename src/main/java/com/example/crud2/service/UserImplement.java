package com.example.crud2.service;

import com.example.crud2.entity.UserEntity;
import com.example.crud2.model.GetResp;
import com.example.crud2.model.PostModel;
import com.example.crud2.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserImplement implements UserService{

    private final UserRepository userRepository;

    public UserImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private ResponseEntity<?> updateResponse(String message) {
        return ResponseEntity
                .ok(Collections
                        .singletonMap("message", message));
    }

    private static ResponseEntity<?> exceptionResponse(String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(Collections
                        .singletonMap("message", message));
    }

    private static ResponseEntity<?> createResponse(String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(Collections.singletonMap("message", message));
    }

    private String HashPwd(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(15));
    }

    private Boolean checkUser(String userName){
        return userRepository.existsByUsernameAndDeletedFalse(userName);
    }

    private Optional<UserEntity> getById(Long id) {
        return userRepository.findById(id);
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
            return getById(id).map(ResponseEntity::ok)
                    .orElseThrow(() ->
                            new NoSuchElementException("Users with ID " + id + " not found"));
        } catch (NoSuchElementException e) {
            return exceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return exceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> saveUser(UserEntity userEntity) {
        try {
            if(checkUser(userEntity.getUsername())) {
                return createResponse("User already exists...", HttpStatus.CONFLICT);
            }

            userEntity.setPassword(HashPwd(userEntity.getPassword()));

            UserEntity user =  userRepository.save(userEntity);
            return createResponse("User has been created", HttpStatus.CREATED);
            } catch (Exception e) {
            return exceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<?> updateUser(Long id, UserEntity userEntity) {
        try {
            return getById(id).map(user -> {
                userRepository.save(userEntity);
                return updateResponse("User with id " + id + " has been updated");
            }).orElseThrow(() ->
                    new NoSuchElementException("User with id " + id + " not found"));
        } catch (NoSuchElementException e) {
            return exceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return exceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> patchUser(Long id, UserEntity userEntity) {
        try {
            return getById(id).map(user -> {
                if (userEntity.getDeleted() != null) {
                    user.setDeleted(userEntity.getDeleted());
                }
                userRepository.save(user);
                return updateResponse("User with id " + id + " has been updated");
            }).orElseThrow(() ->
                    new NoSuchElementException("User with id " + id + " not found"));
        } catch (NoSuchElementException e) {
            return exceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return exceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            return getById(id).map(user -> {
                user.setDeleted(true);
                userRepository.save(user);
                return updateResponse("User with ID " + id + " has been deleted");
            }).orElseThrow(() ->
                    new NoSuchElementException("User with ID " + id + " not found"));
        } catch (NoSuchElementException e) {
            return exceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return exceptionResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
