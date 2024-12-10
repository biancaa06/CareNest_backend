package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.ResetPasswordCode;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetPasswordCodeRepo extends JpaRepository<ResetPasswordCode, Integer> {
    Optional<ResetPasswordCode> findByUser(UserEntity user);
    boolean existsByUser(UserEntity user);
}
