package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class CountryCodeFormatRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        String countryCode = data.getOrDefault("Country", "").trim().toUpperCase();

        boolean isValid = isValidISOAlpha2CountryCode(countryCode);

        if (isValid) {
            return RuleResult.passed(getName(), "✅ Country code is valid: " + countryCode);
        } else {
            return RuleResult.failed(getName(), "❌ Invalid Country code: " + countryCode);
        }
    }

    private boolean isValidISOAlpha2CountryCode(String code) {
        for (String iso : Locale.getISOCountries()) {
            if (iso.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Country Code Format";
    }
}
