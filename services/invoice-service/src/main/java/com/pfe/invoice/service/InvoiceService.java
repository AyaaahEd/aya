package com.pfe.invoice.service;

import com.pfe.invoice.domain.Invoice;
import com.pfe.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice createInvoice(Invoice invoice) {
        invoice.setIssuedAt(LocalDateTime.now());
        if (invoice.getStatus() == null) {
            invoice.setStatus("PENDING");
        }
        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoiceById(String id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public List<Invoice> getInvoicesByUserId(String userId) {
        return invoiceRepository.findByUserId(userId);
    }

    public Invoice getInvoiceByOrderId(String orderId) {
        return invoiceRepository.findByOrderId(orderId);
    }
}
