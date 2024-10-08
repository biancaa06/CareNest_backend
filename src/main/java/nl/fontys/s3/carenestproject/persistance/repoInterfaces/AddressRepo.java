package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
    //public AddressEntity addAddress(AddressEntity address);
    public boolean deleteAddressById(long id);
    AddressEntity findAddressEntityById(long id);
    //boolean deleteAddressEntityById (long id);
}
