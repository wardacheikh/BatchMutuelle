package org.example.batchassurance.Repository;

import org.example.batchassurance.Entity.Beneficiare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficireRepository extends JpaRepository<Beneficiare,String> {
}

