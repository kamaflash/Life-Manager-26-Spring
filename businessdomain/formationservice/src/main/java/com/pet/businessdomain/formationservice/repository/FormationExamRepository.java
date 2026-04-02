package com.pet.businessdomain.formationservice.repository;
import com.pet.businessdomain.formationservice.entities.CharacterExam;
import com.pet.businessdomain.formationservice.entities.FormationExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormationExamRepository extends JpaRepository<FormationExam, Long> {

    List<FormationExam> findByFormationId(Long formationId);

    List<FormationExam> findByFormationIdAndActiveTrue(Long formationId);

}
