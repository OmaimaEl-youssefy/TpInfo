package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Virement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;

import ma.octo.assignement.service.VirementService;

import java.math.BigDecimal;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class VirementControllerTest {

    @MockBean
    VirementService virementService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("if we have content")
    void it_should_return_existed_virements() throws Exception {
        mockMvc.perform(get("/virements/lister-virements"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("if we have no content")
    void it_should_return_existed_virements_no_content() throws Exception {
        mockMvc.perform(get("/virements/lister-virements"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("if we have a virement")
    void get_virementById_virement_exist() throws Exception {

        Compte compteEmetteur = new Compte();
        compteEmetteur.setNrCompte("RIB1");
        Compte compteBeneficiaire = new Compte();
        compteBeneficiaire.setNrCompte("RIB2");
        Virement virement = new Virement();
        virement.setId(1L);
        virement.setMotifVirement("Mon motif");
        virement.setMontantVirement(BigDecimal.ONE);
        virement.setCompteBeneficiaire(compteBeneficiaire);
        virement.setCompteEmetteur(compteEmetteur);

        Mockito.when(virementService.getVirement(any(Long.class))).thenReturn(virement);

        String result = "{'id':1,'nrCompteEmetteur':'RIB1','nrCompteBeneficiaire':'RIB2'," +
                "'motifVirement':'Mon motif','montantVirement':1}";

        mockMvc.perform(get("/virements/get-virement/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(result));

    }

}