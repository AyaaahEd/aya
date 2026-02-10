package com.pfe.invoice;

import com.pfe.invoice.domain.Invoice;
import com.pfe.invoice.repository.InvoiceRepository;
import com.pfe.invoice.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInvoice_shouldSetDateAndStatus() {
        Invoice invoice = new Invoice();
        invoice.setOrderId("order1");
        invoice.setUserId("user1");
        invoice.setAmount(new BigDecimal("100.00"));

        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Invoice savedInvoice = invoiceService.createInvoice(invoice);

        assertNotNull(savedInvoice.getIssuedAt());
        assertEquals("PENDING", savedInvoice.getStatus());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void getInvoiceById_shouldReturnInvoice() {
        Invoice invoice = new Invoice();
        invoice.setId("inv1");
        when(invoiceRepository.findById("inv1")).thenReturn(Optional.of(invoice));

        Invoice foundInvoice = invoiceService.getInvoiceById("inv1");

        assertNotNull(foundInvoice);
        assertEquals("inv1", foundInvoice.getId());
    }

    @Test
    void getInvoiceByOrderId_shouldReturnInvoice() {
        Invoice invoice = new Invoice();
        invoice.setOrderId("order1");
        when(invoiceRepository.findByOrderId("order1")).thenReturn(invoice);

        Invoice foundInvoice = invoiceService.getInvoiceByOrderId("order1");

        assertNotNull(foundInvoice);
        assertEquals("order1", foundInvoice.getOrderId());
    }
}
