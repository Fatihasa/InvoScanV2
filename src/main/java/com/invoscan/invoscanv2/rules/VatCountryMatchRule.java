package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VatCountryMatchRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        String deliveryCountry = data.getOrDefault("Delivery Country", "").trim().toUpperCase();
        String vat = data.getOrDefault("Consignee VAT", "").trim().toUpperCase();
        if (deliveryCountry.isEmpty() || vat.isEmpty()) {
            return RuleResult.failed(getName(), "❌ Delivery Country or Consignee VAT cells empty.");
        }

        System.out.println("deliveryCountry" + deliveryCountry);
        System.out.println("vat" + vat);

        boolean matches = vat.startsWith(deliveryCountry);

        if (matches) {
            return RuleResult.passed(getName(), "✅ VAT no and Delivery Country are matched.");
        } else {
            return RuleResult.failed(getName(),  "❌ VAT no and Delivery Country doesn't match.");
        }
    }

    @Override
    public String getName() {
        return "VAT & Country Match";
    }
}
