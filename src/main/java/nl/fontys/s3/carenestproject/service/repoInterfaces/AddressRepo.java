package nl.fontys.s3.carenestproject.service.repoInterfaces;

import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;

public interface AddressRepo {
    public AddressEntity addAddress(AddressEntity address);
    public AddressEntity getAddressById(long id);
    public boolean deleteAddressById(long id);
}
