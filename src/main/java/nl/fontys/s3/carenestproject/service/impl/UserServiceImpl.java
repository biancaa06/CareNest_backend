package nl.fontys.s3.carenestproject.service.impl;

import lombok.AllArgsConstructor;
import nl.fontys.s3.carenestproject.domain.request.CreateAccountRequest;
import nl.fontys.s3.carenestproject.domain.response.CreateAccountResponse;
import nl.fontys.s3.carenestproject.service.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.service.UserService;
import nl.fontys.s3.carenestproject.service.exception.EmailExistsException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

}
