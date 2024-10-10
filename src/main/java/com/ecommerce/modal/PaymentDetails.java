package com.ecommerce.modal;

import com.ecommerce.Enum.PaymentStatus;
import lombok.*;

@Data
public class PaymentDetails {

    private String paymentId;

    private String razorpayPaymentLinkId;

    private String razorpayPaymentLinkReferenceId;

    private String razorpayPaymentLinkStatus;

    private String razorpayPaymentId;

    private PaymentStatus status;
}
