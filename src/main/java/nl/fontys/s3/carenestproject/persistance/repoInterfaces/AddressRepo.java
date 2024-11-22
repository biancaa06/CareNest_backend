package nl.fontys.s3.carenestproject.persistance.repoInterfaces;

import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {
    boolean deleteAddressById(long id);
    AddressEntity findAddressEntityById(long id);
    boolean existsAddressEntitiesByCountryAndCityAndStreetAndNumber(String country, String city, String street, int number);
    AddressEntity findAddressEntityByCountryAndCityAndStreetAndNumber(String country, String city, String street, int number);
}
