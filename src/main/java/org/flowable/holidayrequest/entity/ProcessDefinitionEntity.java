package org.flowable.holidayrequest.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "process_definitions")
@Getter
@Setter
public class ProcessDefinitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deploymentId;
    private String processDefinitionKey;
    private String workspaceId;
    private String orgId;

}