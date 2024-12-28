package org.among.trsnctest.controller;

import org.among.trsnctest.facade.FacadeExample;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerExample {
    private final FacadeExample facadeExample;

    public ControllerExample(FacadeExample facadeExample) {
        this.facadeExample = facadeExample;
    }

    @PostMapping("/tx-test")
    public String testWithDb() {
        try {
            facadeExample.testWithDb();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException :" + e);
        }

        return facadeExample.selectStatus(1);
    }
}
