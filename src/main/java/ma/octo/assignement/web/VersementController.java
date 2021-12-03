package ma.octo.assignement.web;

import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.dto.VersementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.exceptions.VersementNonExistantException;
import ma.octo.assignement.mapper.VersementMapper;
import ma.octo.assignement.service.VersementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/versements")
public class VersementController {

    private final VersementService versementService;

    @Autowired
    public VersementController(VersementService versementService) {
        this.versementService = versementService;
    }

    @GetMapping("/lister-versements")
    public ResponseEntity<List<VersementDto>> loadAll() {
        List<Versement> all = versementService.loadAll();

        List<VersementDto> response = all.stream().map(VersementMapper::convertToDto).collect(Collectors.toList());

        if (all.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    @PostMapping("/executer-virement")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VersementDto> createTransaction(@RequestBody VersementDto versementDto)
            throws CompteNonExistantException, TransactionException {

        VersementDto response = VersementMapper.mapToDto(versementService.createTransaction(versementDto));

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/get-versement/{id}")
    public ResponseEntity<VersementDto> getVersement(@PathVariable Long id) throws VersementNonExistantException {
        Versement versement = versementService.getVersement(id);
        VersementDto response = VersementMapper.mapToDto(versement);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
