package net.yc.race.track.service;

import net.yc.race.track.model.User;
import net.yc.race.track.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Créer un utilisateur fictif
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");

        // Configurer le comportement du mock
        when(userRepository.save(user)).thenReturn(user);

        // Appeler la méthode et vérifier le résultat
        User registeredUser = userService.registerUser(user);
        assertNotNull(registeredUser);
        assertEquals("testUser", registeredUser.getUsername());

        // Vérifier que save a été appelé une fois
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAuthenticateUserSuccess() {
        // Créer un utilisateur fictif
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");

        // Configurer le comportement du mock
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Appeler la méthode et vérifier le résultat
        Optional<User> authenticatedUser = userService.authenticate("testUser", "password123");
        assertTrue(authenticatedUser.isPresent());
        assertEquals("testUser", authenticatedUser.get().getUsername());

        // Vérifier que findByUsername a été appelé une fois
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testAuthenticateUserFailure() {
        // Configurer le comportement du mock pour retourner un utilisateur non trouvé
        when(userRepository.findByUsername("wrongUser")).thenReturn(Optional.empty());

        // Appeler la méthode et vérifier le résultat
        Optional<User> authenticatedUser = userService.authenticate("wrongUser", "password123");
        assertFalse(authenticatedUser.isPresent());

        // Vérifier que findByUsername a été appelé une fois
        verify(userRepository, times(1)).findByUsername("wrongUser");
    }
}
