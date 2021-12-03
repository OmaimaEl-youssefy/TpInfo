package ma.octo.assignement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.domain.util.EventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditTransactionDto {

    private Long id;
    private String message;
    private EventType eventType;
    private Versement versement;
    private Virement virement;

}
