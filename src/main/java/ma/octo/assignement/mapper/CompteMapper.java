package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.dto.CompteDto;

public class CompteMapper {

    private static CompteDto map(Compte compte) {

        CompteDto compteDto = new CompteDto();
        compteDto.setRib(compte.getRib());
        compteDto.setSolde(compte.getSolde());
        compteDto.setNrCompte(compte.getNrCompte());
        compteDto.setUtilisateur(compte.getUtilisateur());

        return compteDto;
    }

    public static CompteDto convertToDto(Compte compte) {
        return CompteMapper.map(compte);
    }

}
