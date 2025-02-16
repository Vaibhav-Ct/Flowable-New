package org.flowable.holidayrequest.controller;

import lombok.RequiredArgsConstructor;
import org.flowable.holidayrequest.service.DeleteProcessDefinitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/process-definitions")
public class DeleteProcessDefinitionController {

    private final DeleteProcessDefinitionService deleteService;

    @DeleteMapping("/{definitionKey}")
    public ResponseEntity<Void> DeleteProcessDefinitionController(@PathVariable String definitionKey) {
        boolean deleted = deleteService.deleteDefintion(definitionKey);

        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content (Successful Deletion)
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found (If definition doesn't exist)
        }
    }
}
