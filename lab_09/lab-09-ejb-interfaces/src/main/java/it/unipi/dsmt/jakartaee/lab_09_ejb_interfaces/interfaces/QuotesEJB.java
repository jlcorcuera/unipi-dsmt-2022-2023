package it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.interfaces;

import jakarta.ejb.Remote;

@Remote
public interface QuotesEJB {

    String pickOneQuote();

}
