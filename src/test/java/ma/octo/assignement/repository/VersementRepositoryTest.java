package ma.octo.assignement.repository;

import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.domain.Virement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class VersementRepositoryTest {

    @Autowired
    private VersementRepository versementRepository;

    @Test
    void findOne() {

        Versement versement = new Versement();
        versement.setMontantVersement(BigDecimal.TEN);
        versement.setDateExecution(new Date());
        versement.setMotifVersement("motif");
        versementRepository.save(versement);

        Versement newVersement = versementRepository.findById(5L).orElse(null);

        assertThat(newVersement).isNotNull();
        assertThat(newVersement.getId()).isPositive();
        assertThat(newVersement.getMotifVersement()).isEqualTo("motif");
    }

    @Test
    void findAll() {
        Versement versement = new Versement();

        versement.setMontantVersement(BigDecimal.TEN);
        versement.setDateExecution(new Date());
        versement.setMotifVersement("motif");

        versementRepository.save(versement);

        List<Versement> allVersement = versementRepository.findAll();

        assertThat(allVersement.size()).isPositive();
    }

    @Test
    void save() {
        Versement versement = new Versement();
        versement.setMontantVersement(BigDecimal.TEN);
        versement.setDateExecution(new Date());
        versement.setMotifVersement("motif");

        versementRepository.save(versement);

        assertThat(versement).isNotNull();
        assertThat(versement.getId()).isPositive();
    }

    @Test
    void delete() {
        Versement versement = new Versement();
        versement.setMontantVersement(BigDecimal.TEN);
        versement.setDateExecution(new Date());
        versement.setMotifVersement("Test case");
        versementRepository.save(versement);
        versementRepository.delete(versement);
        var deletedVersement = versementRepository.findById(5L).orElse(null);
        assertThat(deletedVersement).isNull();
    }

}
