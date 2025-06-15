package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReferansNoInvoiceNoMatchRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        String j4 = data.get("j4");
        String c5 = data.get("c5");

        if (j4 == null || c5 == null) {
            return RuleResult.failed(getName(),"One of the fields (J4 or C5) is missing.");
        }

        if (j4.trim().equalsIgnoreCase(c5.trim())) {
            return RuleResult.passed(getName(),"✅ Referans No and Invoice No match.");
        } else {
            return RuleResult.failed(getName(),"Referans No and Invoice No do NOT match: " + j4 + " vs " + c5);
        }
    }

    @Override
    public String getName() {
        return "Referans No vs Invoice No Match Rule";
    }
}
