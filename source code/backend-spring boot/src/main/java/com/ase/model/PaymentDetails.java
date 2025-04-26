package com.ase.model;

import com.ase.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

	private String paymentId;
	private String paymentLinkId;
	private PaymentStatus status;

}
