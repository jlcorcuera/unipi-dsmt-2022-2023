package it.unipi.dsmt.microservices.msbeer.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "beer")
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_description")
    private String shortName;

    @Column(name = "description")
    private String description;

    @Column(name = "brand")
    private String brand;

    @Column(name = "price")
    private Float price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "alcolhol_percentage")
    private Float alcoholPercentage;

    @Column(name = "liquid_volume_in_ml")
    private Integer liquidVolumeInML;

}
