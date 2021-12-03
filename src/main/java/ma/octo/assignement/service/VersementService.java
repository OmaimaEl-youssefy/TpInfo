package ma.octo.assignement.service;

import lombok.extern.slf4j.Slf4j;
import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.dto.VersementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.exceptions.VersementNonExistantException;
import ma.octo.assignement.mapper.VersementMapper;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.VersementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class VersementService {

    public static final int MONTANT_MAXIMAL = 10000;
    public static final int MONTANT_MINIMAL = 0;

    private final VersementRepository versementRepository;

    private final AuditTransactionService monservice;

    private final CompteService compteService;

    private final CompteRepository compteRepository;

    @Autowired
    public VersementService(VersementRepository versementRepository, AuditTransactionService monservice, CompteService compteService, CompteRepository compteRepository) {
        this.versementRepository = versementRepository;
        this.monservice = monservice;
        this.compteService = compteService;
        this.compteRepository = compteRepository;
    }


    public List<Versement> loadAll() {
        return versementRepository.findAll();
    }

    public Versement createTransaction(VersementDto versementDto) throws TransactionException, CompteNonExistantException {

        Compte compteBeneficiaire = compteService.getCompteByRib(versementDto.getCompteBeneficiaire().getRib());

        if (compteBeneficiaire == null || compteBeneficiaire.getRib() == null) {
            log.error("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (versementDto.getMontantVersement() == null || versementDto.getMontantVersement().doubleValue() == 0) {
            log.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (versementDto.getMontantVersement().doubleValue() < MONTANT_MINIMAL) {
            log.error("Montant minimal de versement non atteint");
            throw new TransactionException("Montant minimal de versement non atteint");
        } else if (versementDto.getMontantVersement().doubleValue() > MONTANT_MAXIMAL) {
            log.error("Montant maximal de versement dépassé");
            throw new TransactionException("Montant maximal de versement dépassé");
        }

        if (versementDto.getMotifVersement().length() == 0 || versementDto.getMotifVersement().isEmpty()) {
            log.error("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (versementDto.getNomPrenomEmetteur() == null || versementDto.getNomPrenomEmetteur().isEmpty()) {
            log.error("Le nom et le prenom de l'emetteur est vide");
            throw new TransactionException("Le nom et le prenom de l'emetteur est vide");
        }

        compteBeneficiaire.setSolde(BigDecimal.valueOf(compteBeneficiaire.getSolde().doubleValue() + versementDto.getMontantVersement().doubleValue()));

        compteRepository.save(compteBeneficiaire);

        Versement versement = VersementMapper.mapToEntity(versementDto);
        versement.setCompteBeneficiaire(compteBeneficiaire);
        versement.setDateExecution(new Date());

        versementRepository.save(versement);

        monservice.auditVirement("Versement depuis " + versement.getNomPrenomEmetteur() + " vers " + versement
                .getCompteBeneficiaire() + " d'un montant de " + versement.getMontantVersement()
                .toString());

        return versement;
    }

    public Versement getVersement(Long id) throws VersementNonExistantException {
        return versementRepository.findById(id).orElseThrow(() -> new VersementNonExistantException("Le versement n'existe pas"));
    }


}
