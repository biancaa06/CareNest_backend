package nl.fontys.s3.carenestproject.service;


import nl.fontys.s3.carenestproject.domain.classes.Address;

public interface AddressService {
    public Address getAddressById(long id);
    public Address createAddress(Address address);
    public boolean deleteAddressById(long id);
}
