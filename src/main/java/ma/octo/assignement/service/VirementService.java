package ma.octo.assignement.service;

import lombok.extern.slf4j.Slf4j;
import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VirementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.exceptions.VirementNonExistantException;
import ma.octo.assignement.mapper.VirementMapper;
import ma.octo.assignement.repository.VirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class VirementService {

    public static final int MONTANT_MAXIMAL = 10000;
    public static final int MONTANT_MINIMAL = 10;

    private final VirementRepository virementRepository;
    private final CompteService compteService;

    private final AuditTransactionService monservice;

    @Autowired
    public VirementService(VirementRepository virementRepository, CompteService compteService, AuditTransactionService monservice) {
        this.virementRepository = virementRepository;
        this.compteService = compteService;
        this.monservice = monservice;
    }

    public List<Virement> loadAll() {
        List<Virement> all = virementRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return Collections.emptyList();
        } else {
            return all;
        }
    }

    public Virement createTransaction(VirementDto virementDto) throws TransactionException, CompteNonExistantException, SoldeDisponibleInsuffisantException {


        Compte compteEmetteur = compteService.getCompte(virementDto.getNrCompteEmetteur());
        Compte compteBeneficiaire = compteService.getCompte(virementDto.getNrCompteBeneficiaire());

        if (compteEmetteur == null || compteBeneficiaire == null) {
            log.error("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (virementDto.getMontantVirement() == null || virementDto.getMontantVirement().doubleValue() == 0) {
            log.error("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (virementDto.getMontantVirement().doubleValue() < MONTANT_MINIMAL) {
            log.error("Montant minimal de virement non atteint");
            throw new TransactionException("Montant minimal de virement non atteint");
        } else if (virementDto.getMontantVirement().doubleValue() > MONTANT_MAXIMAL) {
            log.error("Montant maximal de virement dépassé");
            throw new TransactionException("Montant maximal de virement dépassé");
        }

        if (virementDto.getMotifVirement().length() == 0 || virementDto.getMotifVirement().isEmpty()) {
            log.error("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (compteEmetteur.getSolde().compareTo(virementDto.getMontantVirement()) == -1) {
            log.error("Solde insuffisant pour l'utilisateur");
            throw new SoldeDisponibleInsuffisantException("Solde insuffisant pour l'utilisateur");
        }
        compteEmetteur.setSolde(compteEmetteur.getSolde().subtract(virementDto.getMontantVirement()));
        compteBeneficiaire.setSolde(BigDecimal.valueOf(compteBeneficiaire.getSolde().doubleValue() + virementDto.getMontantVirement().doubleValue()));

        Virement virement = VirementMapper.mapToEntity(virementDto);
        virement.setDateExecution(new Date());
        virement.setCompteBeneficiaire(compteBeneficiaire);
        virement.setCompteEmetteur(compteEmetteur);
        virement.setMontantVirement(virement.getMontantVirement());
        virement.setMotifVirement(virementDto.getMotifVirement());

        virementRepository.save(virement);

        monservice.auditVirement("Virement depuis " + virement.getCompteEmetteur() + " vers " + virement
                .getCompteBeneficiaire() + " d'un montant de " + virement.getMontantVirement()
                .toString());

        return virement;
    }

    public Virement getVirement(Long id) throws VirementNonExistantException {
        return virementRepository.findById(id).orElseThrow(() -> new VirementNonExistantException("Le virement n'existe pas"));
    }

}
