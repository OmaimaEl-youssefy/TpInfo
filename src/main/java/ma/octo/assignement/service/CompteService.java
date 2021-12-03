package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompteService {


    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public Compte getCompte(String nrCompte) throws CompteNonExistantException {

        Compte compte = compteRepository.findByNrCompte(nrCompte);
        if (compte == null) {
            throw new CompteNonExistantException("Ce compte n'existe pas");
        }
        return compte;
    }

    public Compte getCompteByRib(String rib) throws CompteNonExistantException {
        Compte compte = compteRepository.findByRib(rib);

        if (compte == null) {
            throw new CompteNonExistantException("Ce compte n'existe pas");
        }
        return compte;
    }


    public List<Compte> loadAllCompte() {
        List<Compte> all = compteRepository.findAll();
        return all;
    }

    public Compte addCompte(Compte compte) {
        return compteRepository.save(compte);
    }
}
