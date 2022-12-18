package it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.BeerDTO;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface BeerEJB {
    public List<BeerDTO> search(String keyword);
}
