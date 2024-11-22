package nl.fontys.s3.carenestproject.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
