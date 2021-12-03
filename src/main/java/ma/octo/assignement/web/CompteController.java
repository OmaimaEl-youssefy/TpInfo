package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.dto.CompteDto;
import ma.octo.assignement.mapper.CompteMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ma.octo.assignement.service.CompteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comptes")
public class CompteController {

    private final CompteService compteService;

    public CompteController(CompteService compteService) {
        this.compteService = compteService;
    }

    @GetMapping("/lister-comptes")
    public ResponseEntity<List<CompteDto>> loadAllCompte() {
        List<Compte> all = compteService.loadAllCompte();

        List<CompteDto> response = all.stream().map(CompteMapper::convertToDto).collect(Collectors.toList());

        if (all.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
