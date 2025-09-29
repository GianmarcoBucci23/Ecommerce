package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "la via deve avere almeno 5 caratteri")
    private String street;

    @NotBlank
    @Size(min = 5, message = "edificio deve avere almeno 5 caratteri")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "la città deve avere almeno 5 caratteri")
    private String city;

    @NotBlank
    @Size(min = 2, message = "lo stato deve avere almeno 2 caratteri")
    private String state;

    @NotBlank
    @Size(min = 2, message = "il paese deve avere almeno 2 caratteri")
    private String country;

    @NotBlank
    @Size(min = 6, message = "pincode deve avere almeno 5 caratteri")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String city, String state, String country, String pincode) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pincode = pincode;
    }
}
