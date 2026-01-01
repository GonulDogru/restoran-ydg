package com.restoran.facade;

import com.restoran.model.Superadmin;
import com.restoran.repository.SuperadminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuperadminFacadeUnitTest {

    @Mock
    private SuperadminService superadminService;

    @InjectMocks
    private SuperadminFacade superadminFacade;

    @Test
    void createSuperadmin_whenUsernameNull_shouldReturnError_andNotCallService() {
        Superadmin input = new Superadmin.SuperadminBuilder()
                .setId(0)
                .setUsername(null)
                .setPassword("123456") // null olmasın, facade içinde length() çağrısı var
                .build();

        String result = superadminFacade.createSuperadmin(input);

        assertEquals("Hata: Geçersiz kullanıcı adı veya zayıf şifre.", result);
        verifyNoInteractions(superadminService);
    }

    @Test
    void createSuperadmin_whenPasswordTooShort_shouldReturnError_andNotCallService() {
        Superadmin input = new Superadmin.SuperadminBuilder()
                .setId(0)
                .setUsername("admin")
                .setPassword("123") // < 6
                .build();

        String result = superadminFacade.createSuperadmin(input);

        assertEquals("Hata: Geçersiz kullanıcı adı veya zayıf şifre.", result);
        verifyNoInteractions(superadminService);
    }

    @Test
    void createSuperadmin_whenValid_shouldDelegateToService() {
        Superadmin input = new Superadmin.SuperadminBuilder()
                .setId(0)
                .setUsername("admin")
                .setPassword("123456")
                .build();

        when(superadminService.createSuperadmin(input)).thenReturn("OK");

        String result = superadminFacade.createSuperadmin(input);

        assertEquals("OK", result);
        verify(superadminService, times(1)).createSuperadmin(input);
        verifyNoMoreInteractions(superadminService);
    }

    @Test
    void updateSuperadmin_shouldOverrideIdAndPassFieldsToService() {
        int idFromUrl = 10;

        Superadmin input = new Superadmin.SuperadminBuilder()
                .setId(0) // facade URL'den gelen id'yi set etmeli
                .setUsername("newUser")
                .setPassword("newPass123")
                .build();

        when(superadminService.updateSuperadmin(eq(idFromUrl), any(Superadmin.class)))
                .thenReturn("Superadmin başarıyla güncellendi.");

        String result = superadminFacade.updateSuperadmin(idFromUrl, input);

        assertEquals("Superadmin başarıyla güncellendi.", result);

        ArgumentCaptor<Superadmin> captor = ArgumentCaptor.forClass(Superadmin.class);
        verify(superadminService, times(1)).updateSuperadmin(eq(idFromUrl), captor.capture());

        Superadmin passed = captor.getValue();
        assertEquals(idFromUrl, passed.getId());
        assertEquals("newUser", passed.getUsername());
        assertEquals("newPass123", passed.getPassword());
    }

    @Test
    void deleteSuperadmin_shouldDelegateToService() {
        int id = 5;
        when(superadminService.deleteSuperadmin(id)).thenReturn("Superadmin başarıyla silindi!");

        String result = superadminFacade.deleteSuperadmin(id);

        assertEquals("Superadmin başarıyla silindi!", result);
        verify(superadminService, times(1)).deleteSuperadmin(id);
        verifyNoMoreInteractions(superadminService);
    }

    @Test
    void getAllSuperadminler_shouldDelegateToService() {
        when(superadminService.getAllSuperadminler()).thenReturn(List.of());

        List<Superadmin> result = superadminFacade.getAllSuperadminler();

        assertNotNull(result);
        verify(superadminService, times(1)).getAllSuperadminler();
        verifyNoMoreInteractions(superadminService);
    }
}
