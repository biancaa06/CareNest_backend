package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.service.request.AuthRequest;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.request.UpdateUserAddressRequest;
import nl.fontys.s3.carenestproject.service.response.AuthResponse;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    CreateBaseAccountResponse createUser(CreateBaseAccountRequest request);
    User getUserById(long id);
    void updateUserAddress(UpdateUserAddressRequest request, long userId);
    void updateProfilePicture(MultipartFile file, long userId) throws IOException;
    AuthResponse login(AuthRequest request);
}

