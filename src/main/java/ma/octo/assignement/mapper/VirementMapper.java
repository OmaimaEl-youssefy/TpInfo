package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VirementDto;


import java.util.Date;

public class VirementMapper {

    public static VirementDto mapToDto(Virement virement) {

        VirementDto virementDto = new VirementDto();
        virementDto.setId(virement.getId());
        virementDto.setNrCompteEmetteur(virement.getCompteEmetteur().getNrCompte());
        virementDto.setDateExecution(virement.getDateExecution());
        virementDto.setMotifVirement(virement.getMotifVirement());
        virementDto.setNrCompteBeneficiaire(virement.getCompteBeneficiaire().getNrCompte());
        virementDto.setMontantVirement(virement.getMontantVirement());

        return virementDto;
    }

    public static Virement mapToEntity(VirementDto virementDto) {
        Compte compteBeneficiaire = new Compte();
        compteBeneficiaire.setNrCompte(virementDto.getNrCompteBeneficiaire());

        Compte compteEmetteur = new Compte();
        compteEmetteur.setNrCompte(virementDto.getNrCompteEmetteur());

        Virement virement = new Virement();

        virement.setCompteEmetteur(compteEmetteur);
        virement.setDateExecution(virementDto.getDateExecution() == null ? new Date() : virementDto.getDateExecution());
        virement.setMotifVirement(virementDto.getMotifVirement());
        virement.setCompteBeneficiaire(compteBeneficiaire);
        virement.setMontantVirement(virementDto.getMontantVirement());

        return virement;
    }

}
