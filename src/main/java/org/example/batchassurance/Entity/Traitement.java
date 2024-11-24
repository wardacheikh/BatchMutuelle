package org.example.batchassurance.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Traitement {
    private String codeBarre;
    private boolean existe;
    private String nomMedicament;
    private String typeMedicament;
    private double prixMedicament;


}