package org.sustrav.demo.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "place", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Place implements ImagesDomainAware {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 30)
    private String name;

    @NotNull
    @Size(min = 10, max = 1024)
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Region region;

    @NotNull
    @Column(name = "location", columnDefinition = "Point")
    private Point location;

    @NotNull
    private String imageId;

    private String qrcodeId;

    private int availableFromPoints;

    private int costInPoints;

    @Override
    public void setDomainForImages(String domain) {
        imageId = domain + imageId;
        qrcodeId = domain + qrcodeId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getAvailableFromPoints() {
        return availableFromPoints;
    }

    public void setAvailableFromPoints(int availableFromPoints) {
        this.availableFromPoints = availableFromPoints;
    }

    public int getCostInPoints() {
        return costInPoints;
    }

    public void setCostInPoints(int costInPoints) {
        this.costInPoints = costInPoints;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", region=" + region.getCode() +
                ", location=" + location +
                ", imageId='" + imageId + '\'' +
                ", qrcodeId='" + qrcodeId + '\'' +
                ", availableFromPoints=" + availableFromPoints +
                ", costInPoints=" + costInPoints +
                '}';
    }
}
