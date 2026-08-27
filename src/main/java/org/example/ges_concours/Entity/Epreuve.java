package org.example.ges_concours.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Epreuve {
    @Id
    @GeneratedValue
    private Long id;

    private String nom;

    private Integer coefficient;

    private Integer duree;

    @ManyToOne
    @JoinColumn(name = "concours_id")
    private Concours concours;
}
