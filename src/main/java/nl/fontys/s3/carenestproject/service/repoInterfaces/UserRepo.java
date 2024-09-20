package nl.fontys.s3.carenestproject.service.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;

import java.util.List;

public interface UserRepo {
    public UserEntity getUserByEmail(String email);
    public UserEntity getUserById(long id);
    public List<UserEntity> getAllUsers();
    public UserEntity createUser(UserEntity user);
    public UserEntity updateUser(UserEntity user);
    public boolean deleteUserById(long id);
    public int countUsers();
}
