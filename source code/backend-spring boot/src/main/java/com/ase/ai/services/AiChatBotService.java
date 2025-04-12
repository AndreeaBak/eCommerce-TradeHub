package com.ase.ai.services;

import com.ase.exception.ProductException;
import com.ase.response.ApiResponse;

public interface AiChatBotService {

    ApiResponse aiChatBot(String prompt,Long productId,Long userId) throws ProductException;
}
