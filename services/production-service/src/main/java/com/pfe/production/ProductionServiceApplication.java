package com.pfe.production;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductionServiceApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner commandLineRunner(
            com.pfe.production.service.WorkflowEngine engine) {
        return args -> {
            System.out.println("--- Production Workflow Verification ---");

            System.out.println("Case 1: Border=true, Quality=200");
            System.out.println(engine.getWorkflowForOrder("200", true));

            System.out.println("Case 2: Quality=498");
            System.out.println(engine.getWorkflowForOrder("498", false));

            System.out.println("Case 3: Default");
            System.out.println(engine.getWorkflowForOrder("500", false));
        };
    }

}
