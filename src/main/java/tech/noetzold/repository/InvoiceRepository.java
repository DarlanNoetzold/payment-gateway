package tech.noetzold.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.noetzold.model.CustomerModel;
import tech.noetzold.model.InvoiceModel;
import tech.noetzold.model.PaymentModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InvoiceRepository implements PanacheRepository<InvoiceModel> {

    public Optional<InvoiceModel> findByIdOptional(UUID id) {
        return find("invoiceId", id).firstResultOptional();
    }

    public List<InvoiceModel> findByPaymentId(UUID paymentId) {
        return find("paymentId", paymentId).list();
    }

    public void deleteById(UUID id) {
        delete("invoiceId", id);
    }
}
