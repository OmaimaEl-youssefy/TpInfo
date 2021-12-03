package ma.octo.assignement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.octo.assignement.domain.Utilisateur;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompteDto {

    private Long id;
    private String nrCompte;
    private String rib;
    private BigDecimal solde;
    private Utilisateur utilisateur;

}
