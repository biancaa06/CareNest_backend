package nl.fontys.s3.carenestproject.service.impl;

import nl.fontys.s3.carenestproject.persistance.entity.RefreshToken;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.RefreshTokenRepo;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.exception.TokenExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceImplTest {

    @Mock
    private RefreshTokenRepo mockRefreshTokenRepository;
    @Mock
    private UserRepo mockUserRepository;

    private RefreshTokenServiceImpl refreshTokenServiceImplUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        refreshTokenServiceImplUnderTest = new RefreshTokenServiceImpl(mockRefreshTokenRepository, mockUserRepository);
        ReflectionTestUtils.setField(refreshTokenServiceImplUnderTest, "refreshTokenExpirationMs", 0L);
    }

    @Test
    void testCreateRefreshToken() {
        // Setup
        final RefreshToken expectedResult = RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build();

        // Configure UserRepo.findById(...).
        final Optional<UserEntity> userEntity = Optional.of(UserEntity.builder().build());
        when(mockUserRepository.findById(0L)).thenReturn(userEntity);

        // Configure RefreshTokenRepo.save(...).
        final RefreshToken refreshToken = RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build();
        when(mockRefreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        // Run the test
        final RefreshToken result = refreshTokenServiceImplUnderTest.createRefreshToken(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testCreateRefreshToken_UserRepoReturnsAbsent() {
        // Setup
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> refreshTokenServiceImplUnderTest.createRefreshToken(0L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testVerifyExpiration_TokenIsNotExpired_ShouldReturnTheSame() {
        // Setup
        final RefreshToken token = RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2026, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build();
        final RefreshToken expectedResult = RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2026, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build();

        // Run the test
        final RefreshToken result = refreshTokenServiceImplUnderTest.verifyExpiration(token);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testVerifyExpiration_TokenIsExpired_ThrowsError() {
        // Setup
        final RefreshToken token = RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build();

        TokenExpiredException exception = assertThrows(TokenExpiredException.class,
                () -> refreshTokenServiceImplUnderTest.verifyExpiration(token));

        // Verify that the exception message contains the expected message (ignoring the HTTP status prefix)
        assertTrue(exception.getMessage().contains("Refresh token was expired. Please login again."),
                "Expected exception message to contain: Refresh token was expired. Please login again.");
    }

    @Test
    void testFindByToken() {
        // Setup
        final Optional<RefreshToken> expectedResult = Optional.of(RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build());

        // Configure RefreshTokenRepo.findByToken(...).
        final Optional<RefreshToken> refreshToken = Optional.of(RefreshToken.builder()
                .user(UserEntity.builder().build())
                .token("a75d5698-b7c6-4364-a85c-b9c2227b3a5c")
                .expiryDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build());
        when(mockRefreshTokenRepository.findByToken("refreshToken")).thenReturn(refreshToken);

        // Run the test
        final Optional<RefreshToken> result = refreshTokenServiceImplUnderTest.findByToken("refreshToken");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindByToken_RefreshTokenRepoReturnsAbsent() {
        // Setup
        when(mockRefreshTokenRepository.findByToken("refreshToken")).thenReturn(Optional.empty());

        // Run the test
        final Optional<RefreshToken> result = refreshTokenServiceImplUnderTest.findByToken("refreshToken");

        // Verify the results
        assertThat(result).isEmpty();
    }
}
