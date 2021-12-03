package ma.octo.assignement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VERSEMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Versement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(precision = 16, scale = 2, nullable = false)
    private BigDecimal montantVersement;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExecution;

    @Column
    private String nomPrenomEmetteur;

    @ManyToOne
    @JoinColumn(name = "compteBeneficiaire_id")
    private Compte compteBeneficiaire;

    @Column(length = 200)
    private String motifVersement;

}
