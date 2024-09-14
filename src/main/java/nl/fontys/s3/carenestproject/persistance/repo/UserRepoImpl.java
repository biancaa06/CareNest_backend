package nl.fontys.s3.carenestproject.persistance.repo;

import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.service.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Repository

public abstract class UserRepoImpl implements UserRepo {
}
