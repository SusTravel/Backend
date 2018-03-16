package org.sustrav.demo.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continent", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Continent implements ImagesDomainAware {
    @Id
    @Size(min = 2, max = 10)
    private String code;

    @NotNull
    @Size(min = 4, max = 30)
    private String name;

    @NotNull
    private String imageId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "continent")
    private List<Region> regions = new ArrayList<>();

    @Override
    public void setDomainForImages(String domain) {
        imageId = domain + imageId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Continent{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", imageId='" + imageId + '\'' +
                ", regions=" + regions.size() +
                '}';
    }
}
