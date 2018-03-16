package org.sustrav.demo.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Region implements ImagesDomainAware {
    @Id
    @Size(min = 2, max = 10)
    private String code;

    @NotNull
    @Size(min = 4, max = 30)
    private String name;

    @NotNull
    private String imageId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Continent continent;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "region")
    private List<Place> places = new ArrayList<>();

    @Override
    public void setDomainForImages(String domain) {
        imageId = domain + imageId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "Region{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", continent=" + continent.getCode() +
                ", places=" + places.size() +
                '}';
    }
}
