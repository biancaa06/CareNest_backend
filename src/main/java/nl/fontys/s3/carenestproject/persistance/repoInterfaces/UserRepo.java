package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    /*public UserEntity getUserEntityByEmail(String email);
    public UserEntity getUserById(long id);
    public List<UserEntity> getAllUsers();
    public UserEntity createUser(UserEntity user);
    public UserEntity updateUser(UserEntity user);
    public boolean deleteUserById(long id);
    public int countUsers();*/
}
