package nl.fontys.s3.carenestproject.service;

import nl.fontys.s3.carenestproject.domain.request.CreateAccountRequest;
import nl.fontys.s3.carenestproject.domain.response.CreateAccountResponse;

public interface UserService {
    public CreateAccountResponse addUser(CreateAccountRequest request);
}
