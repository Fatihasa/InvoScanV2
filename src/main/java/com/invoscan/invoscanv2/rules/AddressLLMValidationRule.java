package com.invoscan.invoscanv2.rules;

import com.invoscan.invoscanv2.service.LLMService;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AddressLLMValidationRule implements InvoiceRule {

    @Autowired
    private LLMService llmService;

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        String country = data.getOrDefault("Country", "").trim();
        String city = data.getOrDefault("City", "").trim();
        String postcode = data.getOrDefault("Postcode", "").trim();
        String street = data.getOrDefault("Street", "").trim();

        if (country.isEmpty() || city.isEmpty() || postcode.isEmpty() || street.isEmpty()) {
            return RuleResult.failed(getName(), "❌ Country, City, Postcode veya Street alanı eksik.");
        }

        String prompt = String.format(
                "Does the following address look geographically consistent and realistic?\n" +
                        "Country: %s\n" +
                        "City: %s\n" +
                        "Postcode: %s\n" +
                        "Street and No: %s\n\n" +
                        "Reply with only 'true' or 'false' and explain it with max 3 senctences.",
                country, city, postcode, street
        );

        String response = llmService.askLLM(prompt).trim().toLowerCase();
        System.out.println("🧠 LLM yanıtı (Full Address): " + response);


        boolean passed = response.startsWith("true");

        return passed
                ? RuleResult.passed(getName(), "🧠 LLM response: " + response)
                : RuleResult.failed(getName(), "🧠 LLM response: " + response);
    }

    @Override
    public String getName() {
        return "InvoAI Full Address Validation";
    }
}
