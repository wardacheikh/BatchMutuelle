package org.example.batchassurance.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentDto {

    private String code;
    private String nom;
    private String dci1;
    private String dosage1;
    private String uniteDosage1;
    private String forme;
    private String presentation;
    private String ppv;
    private String ph;
    private String prixBr;
    private String princepsGenerique;
    private String tauxRemboursement;
}
