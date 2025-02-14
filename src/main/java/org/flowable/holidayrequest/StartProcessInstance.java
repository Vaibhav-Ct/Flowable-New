package org.flowable.holidayrequest;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;

public class StartProcessInstance {
    public static RuntimeService start(ProcessEngine processEngine) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("holidayRequest");
//        System.out.println("Process Instance started with ID: " +  runtimeService.createProcessInstanceQuery().processInstanceId("holidayRequest").singleResult().getId());
        return runtimeService;
    }
}
