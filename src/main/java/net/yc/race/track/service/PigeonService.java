package net.yc.race.track.service;

import lombok.RequiredArgsConstructor;
import net.yc.race.track.model.Pigeon;
import net.yc.race.track.model.User;
import net.yc.race.track.repository.PigeonRepository;
import net.yc.race.track.repository.UserRepository;
import net.yc.race.track.serviceInf.PdfExportServiceInf;
import net.yc.race.track.serviceInf.PigeonServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PigeonService implements PigeonServiceInf {


    private final PigeonRepository pigeonRepository;

    private final UserRepository userRepository;

    public String savePigeon(Pigeon pigeon, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return "Utilisateur non trouvé";
        }

        if (pigeon.getCouleur() == null || pigeon.getCouleur().length() < 2) {
            return "Couleur invalide pour le pigeon.";
        }

        String badge = generateUniqueBadge(pigeon);
        pigeon.setNumeroDeBadge(badge);

        pigeonRepository.save(pigeon);
        return "Pigeon enregistré avec succès.";
    }



    public Optional<Pigeon> findPigeonById(Long pigeonId){
        return pigeonRepository.findById(pigeonId);
    }

    // Helper method to generate a unique badge based on color and age
    public String generateUniqueBadge(Pigeon pigeon) {
        String couleur = pigeon.getCouleur();
        if (couleur == null || couleur.length() < 2) {
            throw new IllegalArgumentException("Couleur must be at least 2 characters.");
        }
        return couleur.substring(0, 2).toUpperCase() + System.currentTimeMillis();
    }

    public List<Pigeon> findPigeons(){
        return pigeonRepository.findAll();
    }

    public String deletePigeonById(Long id) {
        if (pigeonRepository.existsById(id)) {
            pigeonRepository.deleteById(id);
            return "Pigeon supprimé avec succès.";
        } else {
            return "Pigeon non trouvé.";
        }
    }
}
