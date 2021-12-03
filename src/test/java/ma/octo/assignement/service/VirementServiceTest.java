package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VirementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.VirementRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class VirementServiceTest {

    @MockBean
    private VirementRepository virementRepository;

    @MockBean
    private CompteService compteService;

    @MockBean
    private AuditTransactionService auditTransactionService;

    @Autowired
    private VirementService virementService;

    private final Date currentDate = new Date();

    @Test
    @DisplayName("get all virements elements")
    void loadAll_virements() {
        List<Virement> virementsList1 = new ArrayList<>();

        Virement virement1 = new Virement();

        virement1.setMotifVirement("mon motif 1");
        virement1.setMontantVirement(BigDecimal.TEN);
        virement1.setCompteEmetteur(new Compte());
        virement1.setCompteBeneficiaire(new Compte());
        virement1.setDateExecution(currentDate);

        Virement virement2 = new Virement();

        virement2.setMotifVirement("mon motif 2");
        virement2.setMontantVirement(BigDecimal.TEN);
        virement2.setCompteEmetteur(new Compte());
        virement2.setCompteBeneficiaire(new Compte());
        virement2.setDateExecution(currentDate);

        virementsList1.add(virement1);
        virementsList1.add(virement2);

        when(virementRepository.findAll()).thenReturn(virementsList1);

        List<Virement> virementList2 = virementService.loadAll();

        Assertions.assertThat(virementsList1).isEqualTo(virementList2);
    }

    @Test
    @DisplayName("create transaction test")
    void createTransactionTest() throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException {

        Compte compteEmetteur = new Compte();
        compteEmetteur.setSolde(BigDecimal.valueOf(1000));

        Compte compteBeneficiaire = new Compte();
        compteBeneficiaire.setSolde(BigDecimal.valueOf(0));

        VirementDto virementDto = new VirementDto();
        virementDto.setNrCompteBeneficiaire("RIB1");
        virementDto.setNrCompteEmetteur("RIB2");
        virementDto.setMontantVirement(BigDecimal.valueOf(100));
        virementDto.setDateExecution(new Date());
        virementDto.setMotifVirement("motif");

        when(compteService.getCompte(virementDto.getNrCompteEmetteur())).thenReturn(compteEmetteur);
        when(compteService.getCompte(virementDto.getNrCompteBeneficiaire())).thenReturn(compteBeneficiaire);

        Virement virement = new Virement();
        virement.setMontantVirement(BigDecimal.valueOf(100));
        virement.setCompteEmetteur(compteEmetteur);
        virement.setCompteBeneficiaire(compteBeneficiaire);
        virement.setDateExecution(virementDto.getDateExecution());
        virement.setMotifVirement(virementDto.getMotifVirement());

        when(virementRepository.save(any(Virement.class))).thenReturn(virement);

        Virement newVirement = virementService.createTransaction(virementDto);

        assertEquals(newVirement.getMontantVirement(), virement.getMontantVirement());
        assertEquals(newVirement.getCompteBeneficiaire().getSolde(), BigDecimal.valueOf(100.0));
        assertEquals(newVirement.getCompteEmetteur().getSolde(), BigDecimal.valueOf(900));

    }

    @Test
    @DisplayName("when montant is less than the minimum montant required")
    void createTransactionTest_Montant_LessThanMinMonant() {

        Throwable exception = assertThrows(TransactionException.class,
                () -> {
                    Compte compteEmetteur = new Compte();
                    compteEmetteur.setSolde(BigDecimal.valueOf(1000));

                    Compte compteBeneficiaire = new Compte();
                    compteBeneficiaire.setSolde(BigDecimal.valueOf(0));

                    VirementDto virementDto = new VirementDto();
                    virementDto.setNrCompteBeneficiaire("RIB1");
                    virementDto.setNrCompteEmetteur("RIB2");
                    virementDto.setMontantVirement(BigDecimal.valueOf(9));
                    virementDto.setDateExecution(new Date());
                    virementDto.setMotifVirement("motif");

                    when(compteService.getCompte(virementDto.getNrCompteEmetteur())).thenReturn(compteEmetteur);
                    when(compteService.getCompte(virementDto.getNrCompteBeneficiaire())).thenReturn(compteBeneficiaire);

                    Virement virement = new Virement();
                    virement.setMontantVirement(BigDecimal.valueOf(9));
                    virement.setCompteEmetteur(compteEmetteur);
                    virement.setCompteBeneficiaire(compteBeneficiaire);
                    virement.setDateExecution(virementDto.getDateExecution());
                    virement.setMotifVirement(virementDto.getMotifVirement());

                    when(virementRepository.save(any(Virement.class))).thenReturn(virement);

                    virementService.createTransaction(virementDto);

                });

        assertEquals(exception.getMessage(), "Montant minimal de virement non atteint");

    }

    @Test
    @DisplayName("when montant is greater than the maximum montant required")
    void createTransactionTest_Montant_GreaterThanMaxMonant() {

        Throwable exception = assertThrows(TransactionException.class,
                () -> {
                    Compte compteEmetteur = new Compte();
                    compteEmetteur.setSolde(BigDecimal.valueOf(10000000));

                    Compte compteBeneficiaire = new Compte();
                    compteBeneficiaire.setSolde(BigDecimal.valueOf(0));

                    VirementDto virementDto = new VirementDto();
                    virementDto.setNrCompteBeneficiaire("RIB1");
                    virementDto.setNrCompteEmetteur("RIB2");
                    virementDto.setMontantVirement(BigDecimal.valueOf(1000000));
                    virementDto.setDateExecution(new Date());
                    virementDto.setMotifVirement("motif");

                    when(compteService.getCompte(virementDto.getNrCompteEmetteur())).thenReturn(compteEmetteur);
                    when(compteService.getCompte(virementDto.getNrCompteBeneficiaire())).thenReturn(compteBeneficiaire);

                    Virement virement = new Virement();
                    virement.setMontantVirement(BigDecimal.valueOf(1000000));
                    virement.setCompteEmetteur(compteEmetteur);
                    virement.setCompteBeneficiaire(compteBeneficiaire);
                    virement.setDateExecution(virementDto.getDateExecution());
                    virement.setMotifVirement(virementDto.getMotifVirement());

                    when(virementRepository.save(any(Virement.class))).thenReturn(virement);

                    virementService.createTransaction(virementDto);

                });

        assertEquals(exception.getMessage(), "Montant maximal de virement dépassé");

    }

    @Test
    @DisplayName("when montant is insufficient")
    void createTransactionTest_Montant_insuffisant() {

        Throwable exception = assertThrows(SoldeDisponibleInsuffisantException.class,
                () -> {
                    Compte compteEmetteur = new Compte();
                    compteEmetteur.setSolde(BigDecimal.valueOf(100));

                    Compte compteBeneficiaire = new Compte();
                    compteBeneficiaire.setSolde(BigDecimal.valueOf(0));

                    VirementDto virementDto = new VirementDto();
                    virementDto.setNrCompteBeneficiaire("RIB1");
                    virementDto.setNrCompteEmetteur("RIB2");
                    virementDto.setMontantVirement(BigDecimal.valueOf(1000));
                    virementDto.setDateExecution(new Date());
                    virementDto.setMotifVirement("motif");

                    when(compteService.getCompte(virementDto.getNrCompteEmetteur())).thenReturn(compteEmetteur);
                    when(compteService.getCompte(virementDto.getNrCompteBeneficiaire())).thenReturn(compteBeneficiaire);

                    Virement virement = new Virement();
                    virement.setMontantVirement(BigDecimal.valueOf(1000000));
                    virement.setCompteEmetteur(compteEmetteur);
                    virement.setCompteBeneficiaire(compteBeneficiaire);
                    virement.setDateExecution(virementDto.getDateExecution());
                    virement.setMotifVirement(virementDto.getMotifVirement());

                    when(virementRepository.save(any(Virement.class))).thenReturn(virement);

                    virementService.createTransaction(virementDto);

                });

        assertEquals(exception.getMessage(), "Solde insuffisant pour l'utilisateur");

    }

    @Test
    @DisplayName("Compte Non existant")
    void createTransactionTest_Compte_Non_existant() {

        Throwable exception = assertThrows(CompteNonExistantException.class,
                () -> {
                    Compte compteEmetteur = compteService.getCompte("RIB33");

                    Compte compteBeneficiaire = compteService.getCompte("RIB22");

                    VirementDto virementDto = new VirementDto();
                    virementDto.setNrCompteBeneficiaire("RIB1");
                    virementDto.setNrCompteEmetteur("RIB2");
                    virementDto.setMontantVirement(BigDecimal.valueOf(1000));
                    virementDto.setDateExecution(new Date());
                    virementDto.setMotifVirement("motif");

                    when(compteService.getCompte(virementDto.getNrCompteEmetteur())).thenReturn(compteEmetteur);
                    when(compteService.getCompte(virementDto.getNrCompteBeneficiaire())).thenReturn(compteBeneficiaire);

                    Virement virement = new Virement();
                    virement.setMontantVirement(BigDecimal.valueOf(1000));
                    virement.setCompteEmetteur(compteEmetteur);
                    virement.setCompteBeneficiaire(compteBeneficiaire);
                    virement.setDateExecution(virementDto.getDateExecution());
                    virement.setMotifVirement(virementDto.getMotifVirement());

                    when(virementRepository.save(any(Virement.class))).thenReturn(virement);

                    virementService.createTransaction(virementDto);

                });

        assertEquals(exception.getMessage(), "Compte Non existant");

    }


}