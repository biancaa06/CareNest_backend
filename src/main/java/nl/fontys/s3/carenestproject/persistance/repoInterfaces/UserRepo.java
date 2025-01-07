package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findUserEntityById(long id);
    UserEntity findUserEntityByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
    SELECT DISTINCT u FROM UserEntity u
    JOIN MessageEntity m ON m.receiver = u OR m.sender = u
    WHERE (m.sender.id = :userId OR m.receiver.id = :userId)
    AND u.id != :userId
    """)
    List<UserEntity> findUserEntitiesWithConversation(Long userId);
}
