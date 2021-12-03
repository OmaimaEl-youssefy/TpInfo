package ma.octo.assignement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.octo.assignement.domain.util.EventType;

import javax.persistence.*;

@Entity
@Table(name = "AUDIT_TRANSACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String message;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

}
