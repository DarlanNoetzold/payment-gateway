package tech.noetzold.model.enums;

public enum PaymentState {
    INIT,
    FRAUD_CHECK_APPROVED,
    FRAUD_CHECK_DENIED,
    WITH_ERROR,
    APPROVED,
    NO_PENDING_ACTION
}
