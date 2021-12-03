package ma.octo.assignement.repository;

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
class VirementRepositoryTest {

    @Autowired
    private VirementRepository virementRepository;

    @Test
    void findOne() {

        Virement virement = new Virement();
        virement.setMontantVirement(BigDecimal.TEN);
        virement.setDateExecution(new Date());
        virement.setMotifVirement("motif");
        virementRepository.save(virement);

        Virement newVirement = virementRepository.findById(5L).orElse(null);

        assertThat(newVirement).isNotNull();
        assertThat(newVirement.getId()).isPositive();
        assertThat(newVirement.getMotifVirement()).isEqualTo("motif");
    }

    @Test
    void findAll() {
        Virement virement = new Virement();

        virement.setMontantVirement(BigDecimal.TEN);
        virement.setDateExecution(new Date());
        virement.setMotifVirement("Test case");

        virementRepository.save(virement);

        List<Virement> allVirements = virementRepository.findAll();

        assertThat(allVirements.size()).isPositive();
    }

    @Test
    void save() {
        Virement virement = new Virement();
        virement.setMontantVirement(BigDecimal.TEN);
        virement.setDateExecution(new Date());
        virement.setMotifVirement("Motif save");

        virementRepository.save(virement);

        assertThat(virement).isNotNull();
        assertThat(virement.getId()).isPositive();
    }

    @Test
    void delete() {
        Virement virement = new Virement();
        virement.setMontantVirement(BigDecimal.TEN);
        virement.setDateExecution(new Date());
        virement.setMotifVirement("Test case");
        virementRepository.save(virement);
        virementRepository.delete(virement);
        var deletedVirement = virementRepository.findById(5L).orElse(null);
        assertThat(deletedVirement).isNull();
    }
}