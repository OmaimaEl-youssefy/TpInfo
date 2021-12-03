package ma.octo.assignement.web;

import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VirementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.exceptions.VirementNonExistantException;
import ma.octo.assignement.mapper.VirementMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ma.octo.assignement.service.VirementService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/virements")
class VirementController {

    private final VirementService virementService;

    VirementController(VirementService virementService) {
        this.virementService = virementService;
    }

    @GetMapping("/lister-virements")
    public ResponseEntity<List<VirementDto>> loadAll() {
        List<Virement> all = virementService.loadAll();

        List<VirementDto> response = all.stream().map(VirementMapper::mapToDto).collect(Collectors.toList());

        if (all.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/executer-virement")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VirementDto> createTransaction(@RequestBody VirementDto virementDto)
            throws CompteNonExistantException, TransactionException, SoldeDisponibleInsuffisantException {

        VirementDto response = VirementMapper.mapToDto(virementService.createTransaction(virementDto));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get-virement/{id}")
    public ResponseEntity<VirementDto> getVirement(@PathVariable Long id) throws VirementNonExistantException {
        Virement virement = virementService.getVirement(id);
        VirementDto response = VirementMapper.mapToDto(virement);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
