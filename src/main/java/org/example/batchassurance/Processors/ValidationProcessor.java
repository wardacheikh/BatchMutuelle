package org.example.batchassurance.Processors;

import org.example.batchassurance.Entity.Dossier;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.stereotype.Component;
@Component
public class ValidationProcessor implements ItemProcessor<Dossier, Dossier> {

    @Override
    public Dossier process(Dossier dossier) throws Exception {
        if (dossier.getNomAssure() == null || dossier.getNumeroAffiliation() == null) {
            throw new ValidationException("Nom ou numéro d'affiliation manquant.");
        }
        if (dossier.getPrixConsultation() <= 0 || dossier.getMontantTotalFrais() <= 0) {
            throw new ValidationException("Prix ou montant total invalide.");
        }
        return dossier;
    }
}