package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.dto.VersementDto;

import java.util.Date;

public class VersementMapper {

    public static VersementDto mapToDto(Versement versement) {

        VersementDto versementDto = new VersementDto();
        versementDto.setId(versement.getId());
        versementDto.setMotifVersement(versement.getMotifVersement());
        versementDto.setCompteBeneficiaire(versement.getCompteBeneficiaire());
        versementDto.setDateExecution(versement.getDateExecution());
        versementDto.setNomPrenomEmetteur(versement.getNomPrenomEmetteur());
        versementDto.setMontantVersement(versement.getMontantVersement());

        return versementDto;
    }

    public static Versement mapToEntity(VersementDto versementDto) {

        Versement versement = new Versement();
        versement.setMotifVersement(versementDto.getMotifVersement());
        versement.setCompteBeneficiaire(versementDto.getCompteBeneficiaire());
        versement.setDateExecution(versementDto.getDateExecution() == null ? new Date() : versementDto.getDateExecution());
        versement.setNomPrenomEmetteur(versementDto.getNomPrenomEmetteur());
        versement.setMontantVersement(versementDto.getMontantVersement());

        return versement;
    }

    public static VersementDto convertToDto(Versement versement) {
        return VersementMapper.mapToDto(versement);
    }

    public static Versement convertToEntity(VersementDto versementDto) {
        return VersementMapper.mapToEntity(versementDto);
    }

}
