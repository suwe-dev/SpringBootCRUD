package com.example.crud2.service;

import com.example.crud2.entity.UserEntity;
import com.example.crud2.model.GetResp;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;


public interface UserService {
    GetResp findAll(int page, int per_page, Sort.Direction orderBy, String sortBy);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> saveUser(UserEntity userEntity);

    ResponseEntity<?> updateUser(Long id, UserEntity userEntity);

    ResponseEntity<?> patchUser(Long id, UserEntity userEntity);

    ResponseEntity<?> deleteUser(Long id);

}
