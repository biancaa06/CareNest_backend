package nl.fontys.s3.carenestproject.persistance.repo;

import nl.fontys.s3.carenestproject.service.repoInterfaces.AddressRepo;
import nl.fontys.s3.carenestproject.service.repoInterfaces.UserRepo;
import nl.fontys.s3.carenestproject.persistance.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Repository
public class UserRepoImpl implements UserRepo {

    private List<UserEntity> users;
    private static long NEXT_ID = 3;

    public UserRepoImpl() {
        AddressRepo addressRepo = new AddressRepoImpl();
        users = new ArrayList<>();
        users.add(UserEntity.builder()
                .id(1)
                .email("email")
                .firstName("first")
                .lastName("last")
                .gender("MALE")
                .address(addressRepo.getAddressById(1))
                .phoneNumber("01456320")
                .build());
        users.add(UserEntity.builder()
                .id(2)
                .email("email2")
                .firstName("donna")
                .lastName("pulsen")
                .gender("FEMALE")
                .address(addressRepo.getAddressById(2))
                .build());
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    @Override
    public UserEntity getUserById(long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        if(!checkExistingEmail(user.getEmail())) {
            user.setId(NEXT_ID++);
            NEXT_ID++;

            users.add(user);
            return user;
        }
        else return null;
    }
    private boolean checkExistingEmail(String email) {
        for (UserEntity user : users) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserEntity updateUser(UserEntity user) {
        UserEntity oldUser = getUserById(user.getId());
        if(oldUser != null) {
            oldUser.setAddress(user.getAddress());
        }
        return user;
    }

    @Override
    public boolean deleteUserById(long id) {
        return users.removeIf(user -> user.getId() == id);
    }

    @Override
    public int countUsers() {
        return users.size();
    }
}
