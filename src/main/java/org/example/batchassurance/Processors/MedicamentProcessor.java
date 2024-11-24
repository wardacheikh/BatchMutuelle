package org.example.batchassurance.Processors;


import org.example.batchassurance.DTO.MedicamentDto;
import org.example.batchassurance.Entity.Medicament;
import org.springframework.batch.item.ItemProcessor;

public class MedicamentProcessor implements ItemProcessor<MedicamentDto, Medicament> {

    @Override
    public Medicament process(MedicamentDto medicamentReferentiel) throws Exception {
        if (medicamentReferentiel == null) {
            return null; // Ignore les entrées nulles
        }

        // Conversion vers l'objet Medicament
        Medicament medicament = new Medicament();
        medicament.setCode(medicamentReferentiel.getCode());
        medicament.setNom(medicamentReferentiel.getNom());
        medicament.setPrix(parseDouble( medicamentReferentiel.getPrixBr())); // On utilise le prix base remboursement
        medicament.setTaux_rembourssement(Double.parseDouble( medicamentReferentiel.getTauxRemboursement().substring(0,medicamentReferentiel.getTauxRemboursement().length()-1)));

        return medicament;
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion : " + value);
            return 0.0; // Valeur par défaut en cas d'erreur
        }
    }
}
