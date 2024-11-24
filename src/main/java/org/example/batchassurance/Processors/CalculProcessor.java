package org.example.batchassurance.Processors;


import org.example.batchassurance.Entity.Beneficiare;
import org.example.batchassurance.Entity.Dossier;
import org.example.batchassurance.Entity.Medicament;
import org.example.batchassurance.Entity.Traitement;
import org.example.batchassurance.Repository.MedicamentReferentielRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CalculProcessor implements ItemProcessor<Dossier, Beneficiare> {

    @Autowired
    private MedicamentReferentielRepository medicamentReferentielRepository;

    @Override
    public Beneficiare process(Dossier dossier) throws Exception {
        double remboursementConsultation = dossier.getPrixConsultation() * 0.7; // Exemple : 70%
        double remboursementTraitements = 0.0;
        double totalApaye=dossier.getPrixConsultation();

        for (Traitement traitement : dossier.getTraitements()) {
            Medicament ref = medicamentReferentielRepository.findById(traitement.getCodeBarre()).orElse(null);
            if (ref != null) {

                remboursementTraitements += (ref.getPrix() * ref.getTaux_rembourssement());
                totalApaye+=traitement.getPrixMedicament();
            }
        }

        Beneficiare beneficiare = new Beneficiare();
        beneficiare.setImmatriculation(dossier.getImmatriculation());
        beneficiare.setNom(dossier.getNomBeneficiaire());
        beneficiare.setDateDepo(dossier.getDateDepotDossier());
        beneficiare.setDateRembourssement(LocalDate.now());
        beneficiare.setTotalPaye(totalApaye);
        double remobourssement = remboursementTraitements+remboursementConsultation;
        beneficiare.setTotalRembourssement(remobourssement);
        return beneficiare;
    }
}