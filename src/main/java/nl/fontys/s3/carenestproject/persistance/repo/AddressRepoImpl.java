package nl.fontys.s3.carenestproject.persistance.repo;

import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.service.repoInterfaces.AddressRepo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class AddressRepoImpl implements AddressRepo {

    private List<AddressEntity> addresses;
    private static long NEXT_ID = 1;

    public AddressRepoImpl() {
        addresses = new ArrayList<>();
        addresses.add(AddressEntity.builder()
                        .id(1L)
                        .country("Netherlands")
                        .city("Eindhoven")
                        .street("Street")
                        .number(56)
                        .build()
                );
        addresses.add(AddressEntity.builder()
                        .id(2L)
                        .country("Netherlands")
                        .city("Eindhoven")
                        .street("Street")
                        .number(48)
                        .build()
                );
    }

    @Override
    public AddressEntity addAddress(AddressEntity address) {
        if(!checkExistingAddress(address)) {
            address.setId(NEXT_ID);
            NEXT_ID++;

            addresses.add(address);

            return address;
        }
        return null;
    }
    private boolean checkExistingAddress(AddressEntity address) {
        for (AddressEntity addressEntity : addresses) {
            if (addressEntity.getCountry().equals(address.getCountry()) &&
                    addressEntity.getCity().equals(address.getCity()) &&
                    addressEntity.getStreet().equals(address.getStreet()) && addressEntity.getNumber()==address.getNumber()) {
                return true;
            }
        }
        return false;
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
