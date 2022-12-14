package it.unipi.dsmt.microservices.msbeer.rest;

import it.unipi.dsmt.microservices.msbeer.dto.BeerDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerFiltersDTO;
import it.unipi.dsmt.microservices.msbeer.dto.BeerSearchDTO;
import it.unipi.dsmt.microservices.msbeer.dto.base.ResponseDTO;
import it.unipi.dsmt.microservices.msbeer.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/beer")
public class BeerRestController {

    @Autowired
    private BeerService beerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseDTO<List<BeerSearchDTO>> search(@RequestParam(name = "name", required = false) String name,
                                                                  @RequestParam(name = "price-start", required = false) Float priceStart,
                                                                  @RequestParam(name = "price-end", required = false) Float priceEnd) {
        BeerFiltersDTO beerFiltersDTO = BeerFiltersDTO.builder()
                        .name(name)
                        .priceStart(priceStart)
                        .priceEnd(priceEnd)
                        .build();
        List<BeerSearchDTO> beerSearchDTOS = beerService.search(beerFiltersDTO);
        return new ResponseDTO<List<BeerSearchDTO>>(beerSearchDTOS);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseDTO<BeerDTO> read(@PathVariable("id") Integer id) {
        BeerDTO dto = beerService.loadById(id);
        return new ResponseDTO<BeerDTO>(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        beerService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping
    public ResponseEntity create(@RequestBody BeerDTO beerDTO) {
        beerService.create(beerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity update(@RequestBody BeerDTO beerDTO) {
        beerService.update(beerDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
