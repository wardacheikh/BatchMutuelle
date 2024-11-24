package org.example.batchassurance.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dossier {
         @Id
        private long id;
        private String nomAssure;
        private String numeroAffiliation;
        private String immatriculation;
        private String lienParente;
        private double montantTotalFrais;
        private double prixConsultation;
        private int nombrePiecesJointes;
        private String nomBeneficiaire;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date dateDepotDossier;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date dateConsultation;
        private List<Traitement> traitements;
    }


