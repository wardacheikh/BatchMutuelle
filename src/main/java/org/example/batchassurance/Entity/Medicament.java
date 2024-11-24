package org.example.batchassurance.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Medicament {
    @Id
    private String code;
    private String nom;
    private double prix;
    private double taux_rembourssement;
}
