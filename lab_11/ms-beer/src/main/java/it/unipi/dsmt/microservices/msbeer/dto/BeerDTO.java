package it.unipi.dsmt.microservices.msbeer.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BeerDTO {

    private Integer id;
    private String name;
    private String shortName;
    private String description;
    private String brand;
    private Float price;
    private String imageUrl;
    private Integer stock;
    private Float alcoholPercentage;
    private Integer liquidVolumeInML;

}
