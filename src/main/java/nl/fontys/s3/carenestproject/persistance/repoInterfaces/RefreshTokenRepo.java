package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
