package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.service.VersementService;
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

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class VersementControllerTest {

    @MockBean
    VersementService versementService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("if we have content")
    void it_should_return_existed_versements() throws Exception {
        mockMvc.perform(get("/versements/lister-versements"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("if we have no content")
    void it_should_return_existed_versements_no_content() throws Exception {
        mockMvc.perform(get("/versements/lister-versements"))
                .andExpect(status().isNoContent());
    }

    @Test
    void get_versementById_versement_exist() throws Exception {

        Compte compteBeneficiaire = new Compte();
        compteBeneficiaire.setRib("RIB1");
        Versement versement = new Versement();
        versement.setId(1L);
        versement.setMotifVersement("Mon motif");
        versement.setMontantVersement(BigDecimal.ONE);
        versement.setCompteBeneficiaire(compteBeneficiaire);
        versement.setNomPrenomEmetteur("omaima");

        Mockito.when(versementService.getVersement(any(Long.class))).thenReturn(versement);

        String result = "{'id':1,'rib':'RIB1'," +
                "'motifVersement':'Mon motif','montantVersement':1,'nomPrenomEmetteur':'omaima'}";

        mockMvc.perform(get("/versements/get-versement/1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(result));

    }

}