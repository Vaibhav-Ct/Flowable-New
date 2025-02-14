package org.flowable.holidayrequest;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendRejectionMail implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        System.out.println("Rejection Service Called: Dear "
                + execution.getVariable("employee") + " Holiday request is rejected for you");
    }
}
