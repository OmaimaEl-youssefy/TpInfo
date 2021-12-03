package ma.octo.assignement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.octo.assignement.domain.Compte;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurDto {

    private Long id;
    private String username;
    private String gender;
    private String lastname;
    private String firstname;
    private List<Compte> comptes;

}
