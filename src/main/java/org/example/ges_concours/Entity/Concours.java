package org.example.ges_concours.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Concours {
    @Id
    @GeneratedValue
    private Long id;

    private String nom ;

    private String description;

    private LocalDate dateLimite;

    private LocalDate dateDeliberation;

}
