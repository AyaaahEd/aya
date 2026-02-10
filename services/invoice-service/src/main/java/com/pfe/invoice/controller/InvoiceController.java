package com.pfe.invoice.controller;

import com.pfe.invoice.domain.Invoice;
import com.pfe.invoice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        return ResponseEntity.ok(invoiceService.createInvoice(invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable String id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public List<Invoice> getInvoicesByUser(@PathVariable String userId) {
        return invoiceService.getInvoicesByUserId(userId);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Invoice> getInvoiceByOrder(@PathVariable String orderId) {
        Invoice invoice = invoiceService.getInvoiceByOrderId(orderId);
        if (invoice != null) {
            return ResponseEntity.ok(invoice);
        }
        return ResponseEntity.notFound().build();
    }
}
