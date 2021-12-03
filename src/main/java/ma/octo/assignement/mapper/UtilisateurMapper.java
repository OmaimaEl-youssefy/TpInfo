package ma.octo.assignement.mapper;

import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.dto.UtilisateurDto;

public class UtilisateurMapper {

    public static UtilisateurDto map(Utilisateur utilisateur) {

        UtilisateurDto utilisateurDto = new UtilisateurDto();
        utilisateurDto.setComptes(utilisateur.getComptes());
        utilisateurDto.setLastname(utilisateur.getLastname());
        utilisateurDto.setFirstname(utilisateur.getFirstname());
        utilisateurDto.setGender(utilisateur.getGender());
        utilisateurDto.setUsername(utilisateur.getUsername());

        return utilisateurDto;

    }

    public static UtilisateurDto convertToDto(Utilisateur utilisateur) {
        return UtilisateurMapper.map(utilisateur);
    }

}
