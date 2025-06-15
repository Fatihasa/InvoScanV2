package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Map;

@Component
public class CurrencyCodeFormatRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        // Currency anahtarını dinamik olarak ara (case-insensitive)
        String currencyCode = data.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().contains("currency"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("")
                .trim()
                .toUpperCase();

        try {
            Currency currency = Currency.getInstance(currencyCode);
            return RuleResult.passed(getName(),  "✅ Currency code is valid: " + currency.getCurrencyCode());
        } catch (IllegalArgumentException e) {
            return RuleResult.failed(getName(), "❌ Currency code is NOT valid: " + currencyCode);
        }
    }

    @Override
    public String getName() {
        return "Currency Code Format";
    }
}
