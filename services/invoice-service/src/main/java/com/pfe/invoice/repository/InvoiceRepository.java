package com.pfe.invoice.repository;

import com.pfe.invoice.domain.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {
    List<Invoice> findByUserId(String userId);

    Invoice findByOrderId(String orderId);
}
