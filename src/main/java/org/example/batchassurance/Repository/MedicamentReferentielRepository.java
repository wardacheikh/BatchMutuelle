package org.example.batchassurance.Repository;

import org.example.batchassurance.Entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentReferentielRepository extends JpaRepository<Medicament,String> {
}
