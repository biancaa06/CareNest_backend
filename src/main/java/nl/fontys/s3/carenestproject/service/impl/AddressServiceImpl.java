package nl.fontys.s3.carenestproject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.service.AddressService;
import nl.fontys.s3.carenestproject.service.mapping.AddressConverter;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AddressRepo;
import org.springframework.stereotype.Service;

@Service
@Builder
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;

    @Override
    public Address getAddressById(long id) {
        AddressEntity addressEntity = addressRepo.findAddressEntityById(id);

        if (addressEntity == null) {
            throw new EntityNotFoundException("Address with ID " + id + " not found.");
        }

        return AddressConverter.convertFromEntityToBase(addressEntity);
    }

    @Override
    public Address createAddress(Address address) {

        AddressEntity addressEntity;
        if(addressRepo.existsAddressEntitiesByCountryAndCityAndStreetAndNumber(address.getCountry(), address.getCity(), address.getStreet(), address.getNumber())) {
            addressEntity = addressRepo.findAddressEntityByCountryAndCityAndStreetAndNumber(address.getCountry(), address.getCity(), address.getStreet(), address.getNumber());
        }
        else{
            addressEntity = addressRepo.save(AddressConverter.convertFromBaseToEntity(address));
        }


        if (addressEntity == null) {
            throw new IllegalStateException("Failed to save address.");
        }

        address.setId(addressEntity.getId());
        return address;
    }

    @Override
    public boolean deleteAddressById(long id) {
        return addressRepo.deleteAddressById(id);
    }
}
