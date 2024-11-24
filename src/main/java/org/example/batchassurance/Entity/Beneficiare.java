package org.example.batchassurance.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beneficiare {

    @Id
    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "date_depo")
    private java.util.Date dateDepo;

    @Column(name = "date_rembourssement")
    private LocalDate dateRembourssement;

    @Column(name = "total_paye", nullable = false)
    private double totalPaye;

    @Column(name = "total_rembourssement", nullable = false)
    private double totalRembourssement;

}
