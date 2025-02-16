package org.flowable.holidayrequest.config;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessEngineConfig {
    @Bean
    public ProcessEngine processEngine(){
        return new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:postgresql://localhost:5432/orchestration-poc")
                .setJdbcUsername("admin")
                .setJdbcPassword("test")
                .setJdbcDriver("org.postgresql.Driver")
                .setDatabaseSchemaUpdate("true")
                .buildProcessEngine();
    }
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }
    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }
    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }
    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }
}
