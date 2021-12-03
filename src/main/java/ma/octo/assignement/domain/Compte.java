package ma.octo.assignement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "COMPTE")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 16, unique = true)
    private String nrCompte;

    @Column(nullable = false, unique = true)
    private String rib;

    @Column(precision = 16, scale = 2)
    private BigDecimal solde;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "compteBeneficiaire")
    private List<Versement> versementsCompteEmetteur;

    @OneToMany(mappedBy = "compteEmetteur")
    private List<Virement> virementsCompteEmetteur;

    @OneToMany(mappedBy = "compteBeneficiaire")
    private List<Virement> virementscompteBeneficiaire;

}
