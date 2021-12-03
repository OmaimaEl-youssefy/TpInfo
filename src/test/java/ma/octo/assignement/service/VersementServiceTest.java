package ma.octo.assignement.service;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.dto.VersementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.VersementRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class VersementServiceTest {

    @MockBean
    private VersementRepository versementRepository;

    @MockBean
    private CompteRepository compteRepository;

    @MockBean
    private CompteService compteService;

    @MockBean
    private AuditTransactionService auditTransactionService;


    private VersementService versementService;


    private final Date currentDate = new Date();

    @BeforeEach
    void setUp() {
        versementService = new VersementService(versementRepository, auditTransactionService, compteService, compteRepository);
    }


    @Test
    @DisplayName("get all versements elements")
    void loadAll_versements() {
        List<Versement> versementList1 = new ArrayList<>();

        Versement versement1 = new Versement();
        versement1.setMontantVersement(BigDecimal.TEN);
        versement1.setNomPrenomEmetteur("omaima el-youssefy");
        versement1.setDateExecution(currentDate);
        versement1.setCompteBeneficiaire(new Compte());
        versement1.setMotifVersement("mon motif 1");

        Versement versement2 = new Versement();
        versement1.setMontantVersement(BigDecimal.TEN);
        versement1.setNomPrenomEmetteur("el-youssefy omaima");
        versement1.setDateExecution(currentDate);
        versement1.setCompteBeneficiaire(new Compte());
        versement1.setMotifVersement("mon motif 2");

        versementList1.add(versement1);
        versementList1.add(versement2);

        when(versementRepository.findAll()).thenReturn(versementList1);

        List<Versement> versementList2 = versementService.loadAll();

        Assertions.assertThat(versementList1).isEqualTo(versementList2);

    }

    @Test
    @DisplayName("create transaction test")
    void createTransactionTest() throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException {

        Compte compteBeneficiaire = new Compte();
        compteBeneficiaire.setSolde(BigDecimal.valueOf(0));
        compteBeneficiaire.setRib("RIB1");

        VersementDto versementDto = new VersementDto();
        versementDto.setNomPrenomEmetteur("omaima el-youssefy");
        versementDto.setCompteBeneficiaire(compteBeneficiaire);
        versementDto.setMontantVersement(BigDecimal.valueOf(100));
        versementDto.setDateExecution(new Date());
        versementDto.setMotifVersement("motif");


        when(compteService.getCompteByRib(versementDto.getCompteBeneficiaire().getRib())).thenReturn(compteBeneficiaire);

        Versement versement = new Versement();
        versement.setMontantVersement(versementDto.getMontantVersement());
        versement.setCompteBeneficiaire(compteBeneficiaire);
        versement.setDateExecution(versementDto.getDateExecution());
        versement.setMotifVersement(versementDto.getMotifVersement());
        versement.setNomPrenomEmetteur(versementDto.getNomPrenomEmetteur());

        when(versementRepository.save(any(Versement.class))).thenReturn(versement);

        Versement newVersement = versementService.createTransaction(versementDto);

        assertEquals(newVersement.getMontantVersement(), versement.getMontantVersement());
        assertEquals(newVersement.getCompteBeneficiaire().getSolde(), BigDecimal.valueOf(100.0));

    }

    @Test
    @DisplayName("Le monatnt est vide")
    void createTransactionTest_montant_vide() {

        Throwable exception = assertThrows(TransactionException.class, () -> {

            Compte compteBeneficiaire = new Compte();
            compteBeneficiaire.setSolde(BigDecimal.valueOf(0));
            compteBeneficiaire.setRib("RIB1");

            VersementDto versementDto = new VersementDto();
            versementDto.setNomPrenomEmetteur("omaima el-youssefy");
            versementDto.setCompteBeneficiaire(compteBeneficiaire);
            versementDto.setMontantVersement(BigDecimal.ZERO);
            versementDto.setDateExecution(new Date());
            versementDto.setMotifVersement("motif");

            when(compteService.getCompteByRib(versementDto.getCompteBeneficiaire().getRib())).thenReturn(compteBeneficiaire);

            Versement versement = new Versement();
            versement.setMontantVersement(versementDto.getMontantVersement());
            versement.setCompteBeneficiaire(compteBeneficiaire);
            versement.setDateExecution(versementDto.getDateExecution());
            versement.setMotifVersement(versementDto.getMotifVersement());
            versement.setNomPrenomEmetteur(versementDto.getNomPrenomEmetteur());

            when(versementRepository.save(any(Versement.class))).thenReturn(versement);

            versementService.createTransaction(versementDto);
        });
        assertEquals(exception.getMessage(), "Montant vide");
    }


}