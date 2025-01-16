package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.MessageEntity;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepo extends JpaRepository<MessageEntity, Integer> {
    @Query("""
    SELECT m
    FROM MessageEntity m
    WHERE (m.receiver = :receiver AND m.sender = :sender) OR (m.receiver = :sender AND m.sender = :receiver)
    """)
    Page<MessageEntity> findMessageEntitiesByParticipants(UserEntity sender, UserEntity receiver, Pageable pageable);
}
