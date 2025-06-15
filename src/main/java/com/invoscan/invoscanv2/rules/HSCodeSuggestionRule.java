package com.invoscan.invoscanv2.rules;

import com.invoscan.invoscanv2.service.LLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HSCodeSuggestionRule {

    @Autowired
    private LLMService llmService;

    public String ask(String description) {
        String fullPrompt = """
You are an expert in customs and tariff classifications using the TARBEL system (Belgium).
Please examine the product description below and provide ONLY the most relevant HS code (Harmonized System code).
Then, briefly explain in 2-3 sentences why this HS code is appropriate according to TARBEL logic.

Product Description:
""" + description;

        return llmService.askLLM(fullPrompt);
    }
}
