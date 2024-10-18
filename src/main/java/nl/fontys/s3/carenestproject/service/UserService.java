package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.classes.users.User;
import nl.fontys.s3.carenestproject.service.request.CreateBaseAccountRequest;
import nl.fontys.s3.carenestproject.service.response.CreateBaseAccountResponse;

public interface UserService {
    CreateBaseAccountResponse createUser(CreateBaseAccountRequest request);
    User getUserById(long id);
}
