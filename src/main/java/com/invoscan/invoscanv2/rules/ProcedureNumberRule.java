package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProcedureNumberRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        String procedure = data.get("Procedure Number");

        // 🔍 Debug - Anahtarları ve değeri kontrol edelim
        System.out.println("🔍 Mevcut Anahtarlar: " + data.keySet());
        System.out.println("🔍 Procedure Number değeri: " + procedure);

        if (procedure == null || procedure.isBlank()) {
            return RuleResult.failed(
                    "Procedure Number",
                    "❌ Procedure alanı boş."
            );
        }

        if (!procedure.trim().equals("4200")) {
            return RuleResult.failed(
                    " Procedure Number",
                    "❌ Procedure number must be 4200."
            );
        }

        return RuleResult.passed(
                "Procedure Number",
                "✅ Procedure number is correct."
        );
    }

    @Override
    public String getName() {
        return "Procedure Number";
    }
}
