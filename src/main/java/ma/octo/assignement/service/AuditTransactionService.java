package ma.octo.assignement.service;

import ma.octo.assignement.domain.AuditTransaction;
import ma.octo.assignement.domain.util.EventType;
import ma.octo.assignement.repository.AuditTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuditTransactionService {

    Logger logger = LoggerFactory.getLogger(AuditTransactionService.class);

    private final AuditTransactionRepository auditVirementRepository;

    @Autowired
    public AuditTransactionService(AuditTransactionRepository auditVirementRepository) {
        this.auditVirementRepository = auditVirementRepository;
    }

    public void auditVirement(String message) {

        logger.info("Audit de l'événement {}", EventType.VIREMENT);

        AuditTransaction audit = new AuditTransaction();
        audit.setEventType(EventType.VIREMENT);
        audit.setMessage(message);
        auditVirementRepository.save(audit);
    }

    public void auditVersement(String message) {

        logger.info("Audit de l'événement {}", EventType.VERSEMENT);

        AuditTransaction audit = new AuditTransaction();
        audit.setEventType(EventType.VERSEMENT);
        audit.setMessage(message);
        auditVirementRepository.save(audit);
    }

}
