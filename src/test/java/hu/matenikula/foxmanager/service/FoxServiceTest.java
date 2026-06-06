package hu.matenikula.foxmanager.service;

import hu.matenikula.foxmanager.domain.Fox;
import hu.matenikula.foxmanager.domain.Gender;
import hu.matenikula.foxmanager.exception.FoxNotFoundException;
import hu.matenikula.foxmanager.repository.FoxRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoxServiceTest {

    @Mock
    private FoxRepository foxRepository;

    @InjectMocks
    private FoxService foxService;

    @Test
    void createFox_savesAndReturnsFox() {
        //given
        Fox input = new Fox();
        input.setName("Vuk");
        input.setGender(Gender.MALE);

        Fox saved = new Fox();
        saved.setId(1L);
        saved.setName("Vuk");
        when(foxRepository.save(input)).thenReturn(saved);

        //when
        Fox result = foxService.createFox(input);

        //then
        assertEquals(1L, result.getId());
        verify(foxRepository).save(input);
    }

    @Test
    void getFoxById_found_returnsFox() {
        //given
        Fox fox = new Fox();
        fox.setId(2L);

        //when
        when(foxRepository.findById(2L)).thenReturn(Optional.of(fox));

        //then
        assertSame(fox, foxService.getFoxById(2L));
    }

    @Test
    void getFoxById_notFound_throws() {
        //when
        when(foxRepository.findById(99L)).thenReturn(Optional.empty());

        //then
        assertThrows(FoxNotFoundException.class, () -> foxService.getFoxById(99L));
    }

    @Test
    void deleteFox_found_deletes() {
        //when
        when(foxRepository.deleteById(1L)).thenReturn(true);

        //then
        assertDoesNotThrow(() -> foxService.deleteFox(1L));
        verify(foxRepository).deleteById(1L);
    }

    @Test
    void deleteFox_notFound_throws() {
        //when
        when(foxRepository.deleteById(99L)).thenReturn(false);

        //then
        assertThrows(FoxNotFoundException.class, () -> foxService.deleteFox(99L));
    }

    @Test
    void updateFox_found_updatesFields() {
        //given
        Fox existing = new Fox();
        existing.setId(1L);
        existing.setName("Régi");
        when(foxRepository.findById(1L)).thenReturn(Optional.of(existing));

        Fox update = new Fox();
        update.setId(1L);
        update.setName("Új");
        update.setSpecies("Vörös róka");
        update.setGender(Gender.FEMALE);

        //when
        foxService.updateFox(update);

        //then
        assertEquals("Új", existing.getName());
        assertEquals("Vörös róka", existing.getSpecies());
        assertEquals(Gender.FEMALE, existing.getGender());
    }

    @Test
    void updateFox_notFound_throws() {
        //given
        Fox update = new Fox();
        update.setId(99L);

        //when
        when(foxRepository.findById(99L)).thenReturn(Optional.empty());

        //then
        assertThrows(FoxNotFoundException.class, () -> foxService.updateFox(update));
    }

    @Test
    void getAllFoxes_returnsRepositoryList() {
        //when
        when(foxRepository.findAll()).thenReturn(List.of(new Fox(), new Fox()));

        //then
        assertEquals(2, foxService.getAllFoxes().size());
    }

    @Test
    void assignImage_found_setsImage() {
        //given
        Fox fox = new Fox();
        fox.setId(1L);
        when(foxRepository.findById(1L)).thenReturn(Optional.of(fox));

        //when
        foxService.assignImage(1L, "http://img/1.jpg");

        //then
        assertEquals("http://img/1.jpg", fox.getImage());
    }
}