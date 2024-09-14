package nl.fontys.s3.carenestproject.persistance.repo;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.service.repoInterfaces.AddressRepo;

import java.util.List;

@SuperBuilder
@Data
public class AddressRepoImpl implements AddressRepo {

    private List<AddressEntity> addresses;
    private static long NEXT_ID = 1;

    @Override
    public AddressEntity addAddress(AddressEntity address) {
        address.setId(NEXT_ID);
        NEXT_ID++;

        addresses.add(address);

        return address;
    }

    @Override
    public AddressEntity getAddressById(long id) {
        return addresses.stream()
                .filter(address -> address.getId() == id).findFirst().orElse(null);
    }

    @Override
    public boolean deleteAddressById(long id) {
        return false;
    }
}
