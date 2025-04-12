package com.ase.response;

import com.ase.dto.OrderHistory;
import com.ase.model.Cart;
import com.ase.model.Product;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunctionResponse {
    private String functionName;
    private Cart userCart;
    private OrderHistory orderHistory;
    private Product product;
}
