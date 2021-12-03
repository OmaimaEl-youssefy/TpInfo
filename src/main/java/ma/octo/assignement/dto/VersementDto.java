package ma.octo.assignement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.octo.assignement.domain.Compte;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VersementDto {

    private Long id;
    private BigDecimal montantVersement;
    private Date dateExecution;
    private String nomPrenomEmetteur;
    private Compte compteBeneficiaire;
    private String motifVersement;

}
