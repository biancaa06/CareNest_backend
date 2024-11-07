package nl.fontys.s3.carenestproject.service.impl;

import jakarta.persistence.EntityNotFoundException;
import nl.fontys.s3.carenestproject.domain.classes.Address;
import nl.fontys.s3.carenestproject.persistance.entity.AddressEntity;
import nl.fontys.s3.carenestproject.persistance.repoInterfaces.AddressRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAddressById_ShouldReturnAddress_WhenIdIsValid() {
        long id = 1L;
        AddressEntity addressEntity = AddressEntity.builder()
                .id(id)
                .country("Netherlands")
                .city("Eindhoven")
                .street("Fontys Street")
                .number(10)
                .build();

        when(addressRepo.findAddressEntityById(id)).thenReturn(addressEntity);

        Address result = addressService.getAddressById(id);

        assertNotNull(result);
        assertEquals("Netherlands", result.getCountry());
        assertEquals("Eindhoven", result.getCity());
        verify(addressRepo, times(1)).findAddressEntityById(id);
    }

    @Test
    void getAddressById_ShouldThrowException_WhenIdIsInvalid() {
        long id = 0L;
        when(addressRepo.findAddressEntityById(id)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> addressService.getAddressById(id));
    }

    @Test
    void createAddress_ShouldReturnAddressWithId_WhenSavedSuccessfully() {
        Address address = Address.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .street("Fontys Street")
                .number(10)
                .build();

        AddressEntity addressEntity = AddressEntity.builder()
                .id(1L)
                .country("Netherlands")
                .city("Eindhoven")
                .street("Fontys Street")
                .number(10)
                .build();

        when(addressRepo.save(any(AddressEntity.class))).thenReturn(addressEntity);

        Address result = addressService.createAddress(address);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(addressRepo, times(1)).save(any(AddressEntity.class));
    }

    @Test
    void createAddress_ShouldThrowException_WhenSaveFails() {
        Address address = Address.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .street("Fontys Street")
                .number(10)
                .build();

        when(addressRepo.save(any(AddressEntity.class))).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> addressService.createAddress(address),
                "Expected IllegalStateException when address creation fails");

        verify(addressRepo, times(1)).save(any(AddressEntity.class));
    }

    @Test
    void deleteAddressById_ShouldReturnTrue_WhenDeletionIsSuccessful() {
        long id = 1L;
        when(addressRepo.deleteAddressById(id)).thenReturn(true);

        boolean result = addressService.deleteAddressById(id);

        assertTrue(result);
        verify(addressRepo, times(1)).deleteAddressById(id);
    }

    @Test
    void deleteAddressById_ShouldReturnFalse_WhenDeletionFails() {
        long id = 1L;
        when(addressRepo.deleteAddressById(id)).thenReturn(false);

        boolean result = addressService.deleteAddressById(id);

        assertFalse(result, "Expected false when deletion fails");
        verify(addressRepo, times(1)).deleteAddressById(id);
    }
}
