package ma.octo.assignement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VIREMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Virement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal montantVirement;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExecution;

    @ManyToOne
    @JoinColumn(name = "compteEmetteur_id")
    private Compte compteEmetteur;

    @ManyToOne
    @JoinColumn(name = "compteBeneficiaire")
    private Compte compteBeneficiaire;

    @Column(length = 200)
    private String motifVirement;

}
