package ma.octo.assignement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VirementDto {

    private Long id;
    private String nrCompteEmetteur;
    private String nrCompteBeneficiaire;
    private String motifVirement;
    private BigDecimal montantVirement;
    private Date dateExecution;

}
