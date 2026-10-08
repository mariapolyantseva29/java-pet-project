package com.bright.auth_service.service;

import com.bright.auth_service.entity.UserEntity;
import com.bright.auth_service.record.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class MapperService {
    public UserRecord toUserRecord(UserEntity userEntity) {
        return new UserRecord(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getPassword(),
                userEntity.getCreateDate(),
                userEntity.getUpdateDate()
        );
    }
}
