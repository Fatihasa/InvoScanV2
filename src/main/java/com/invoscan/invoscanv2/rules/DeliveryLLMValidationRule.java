package com.invoscan.invoscanv2.rules;

import com.invoscan.invoscanv2.service.LLMService;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeliveryLLMValidationRule implements InvoiceRule {

    @Autowired
    private LLMService llmService;

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        String country = data.getOrDefault("Delivery Country", "").trim(); // C17
        String city = data.getOrDefault("Delivery City", "").trim();       // J15

        if (country.isEmpty() || city.isEmpty()) {
            return RuleResult.failed(getName(), "❌ Delivery Country (C17) veya City (J15) boş.");
        }

        String prompt = String.format(
                "Is the city '%s' located in the country '%s'? Answer only with 'true' or 'false' and explain in one sentence.",
                city, country
        );

        String response = llmService.askLLM(prompt).trim().toLowerCase();
        boolean passed = response.contains("true");

        return RuleResult.passed(getName(), "🧠 LLM response: " + response);
    }

    @Override
    public String getName() {
        return "LLM Delivery Country-City Match";
    }
}
