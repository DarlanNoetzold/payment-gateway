package tech.noetzold.service;

import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tech.noetzold.model.InvoiceModel;
import tech.noetzold.repository.InvoiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InvoiceService {

    @Inject
    InvoiceRepository invoiceRepository;

    @Transactional
    @CacheResult(cacheName = "invoice")
    public InvoiceModel findInvoiceById(UUID id) {
        Optional<InvoiceModel> optionalInvoice = invoiceRepository.findByIdOptional(id);
        return optionalInvoice.orElse(null);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "invoice")
    public InvoiceModel saveInvoice(InvoiceModel invoiceModel) {
        invoiceRepository.persist(invoiceModel);
        invoiceRepository.flush();
        return invoiceModel;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "invoice")
    public void updateInvoice(InvoiceModel invoiceModel) {
        if (invoiceModel == null || invoiceModel.getId() == null) {
            throw new WebApplicationException("Invalid data for invoice update", Response.Status.BAD_REQUEST);
        }

        InvoiceModel existingInvoice = findInvoiceById(invoiceModel.getId());
        if (existingInvoice == null) {
            throw new WebApplicationException("Invoice not found", Response.Status.NOT_FOUND);
        }

        existingInvoice.setInvoiceNumber(invoiceModel.getInvoiceNumber());
        existingInvoice.setInvoiceDate(invoiceModel.getInvoiceDate());
        existingInvoice.setTotalAmount(invoiceModel.getTotalAmount());
        existingInvoice.setDiscountAmount(invoiceModel.getDiscountAmount());

        invoiceRepository.persist(existingInvoice);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "invoice")
    public void deleteInvoiceById(UUID id) {
        invoiceRepository.deleteById(id);
    }

    @Transactional
    @CacheResult(cacheName = "invoice")
    public List<InvoiceModel> findInvoicesByPaymentId(String paymentId) {
        return invoiceRepository.findByPaymentId(paymentId);
    }
}
