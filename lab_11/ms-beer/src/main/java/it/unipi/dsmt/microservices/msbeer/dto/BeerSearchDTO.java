package it.unipi.dsmt.microservices.msbeer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerSearchDTO {
    private Integer id;
    private String name;
    private String shortName;
    private Float price;
    private String imageUrl;
}
