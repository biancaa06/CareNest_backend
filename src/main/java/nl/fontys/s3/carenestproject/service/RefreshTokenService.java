package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.persistance.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String refreshToken);

}
