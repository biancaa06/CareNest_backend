package nl.fontys.s3.carenestproject.service.impl;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.repoInterfaces.AddressRepo;
import org.springframework.stereotype.Service;

@Service
@SuperBuilder
@Data
public class AddressServiceImpl implements AddressService {

    private AddressRepo addressRepo;

    @Override
    public Address getAddressById(long id) {
        AddressEntity addressEntity = addressRepo.getAddressById(id);

        return Address.builder()
                .id(addressEntity.getId())
                .Country(addressEntity.getCountry())
                .City(addressEntity.getCity())
                .Street(addressEntity.getStreet())
                .number(addressEntity.getNumber())
                .build();
    }

    @Override
    public Address createAddress(Address address) {

        AddressEntity addressEntity = AddressEntity.builder()
                .Country(address.getCountry())
                .City(address.getCity())
                .Street(address.getStreet())
                .number(address.getNumber()).
                build();

        addressEntity = addressRepo.addAddress(addressEntity);

        address.setId(addressEntity.getId());
        return address;
    }

    @Override
    public boolean deleteAddressById(long id) {
        return addressRepo.deleteAddressById(id);
    }
}
