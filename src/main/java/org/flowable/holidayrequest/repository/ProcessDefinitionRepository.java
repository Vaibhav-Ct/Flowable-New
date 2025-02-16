package org.flowable.holidayrequest.repository;

import org.flowable.holidayrequest.entity.ProcessDefinitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinitionEntity, Long> {
    ProcessDefinitionEntity findByDeploymentId(String deploymentId);
}