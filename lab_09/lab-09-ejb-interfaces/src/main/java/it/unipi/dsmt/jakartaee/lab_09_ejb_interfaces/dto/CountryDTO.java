package it.unipi.dsmt.jakartaee.lab_09_ejb_interfaces.dto;

import java.io.Serializable;

public class CountryDTO implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
