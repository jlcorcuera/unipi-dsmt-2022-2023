package it.unipi.dsmt.javaee.lab_07.dto;

public class BeerDTO {

    private String name;
    private String link;
    private String imageUrl;
    private Double rating;

    public BeerDTO(String name, String link, String imageUrl, Double rating) {
        this.name = name;
        this.link = link;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
