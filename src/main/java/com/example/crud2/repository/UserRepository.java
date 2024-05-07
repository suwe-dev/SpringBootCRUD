    package com.example.crud2.repository;

    import com.example.crud2.entity.UserEntity;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.repository.PagingAndSortingRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>, JpaRepository<UserEntity, Long> {
        Page<UserEntity> findByDeletedFalse(PageRequest pageRequest);
        Boolean existsByUsernameAndDeletedFalse(String username);
    }
