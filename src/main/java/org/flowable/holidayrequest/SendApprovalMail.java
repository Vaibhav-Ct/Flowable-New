package org.flowable.holidayrequest;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendApprovalMail implements JavaDelegate {

    public void execute(DelegateExecution execution) {
        System.out.println("Approval Service Called: Dear "
                + execution.getVariable("employee") + " Holiday request is approved for you");
    }

}