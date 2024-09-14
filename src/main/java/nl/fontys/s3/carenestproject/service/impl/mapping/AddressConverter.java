package nl.fontys.s3.carenestproject.service.impl.mapping;

import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;

public class AddressConverter {
    private AddressConverter() {}

    public static Address convertFromEntityToBase(AddressEntity addressEntity) {
        return Address.builder()
                .id(addressEntity.getId())
                .country(addressEntity.getCountry())
                .city(addressEntity.getCity())
                .street(addressEntity.getStreet())
                .number(addressEntity.getNumber())
                .build();
    }

    public static AddressEntity convertFromBaseToEntity(Address address) {
        return AddressEntity.builder()
                .id(address.getId())
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .number(address.getNumber())
                .build();
    }
}
