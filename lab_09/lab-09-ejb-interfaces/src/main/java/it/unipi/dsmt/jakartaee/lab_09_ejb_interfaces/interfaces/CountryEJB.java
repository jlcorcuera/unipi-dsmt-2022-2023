package it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces;

import it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto.CountryDTO;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface CountryEJB {

    List<CountryDTO> list();
}
