package com.invoscan.invoscanv2;

import com.invoscan.invoscanv2.reader.ExcelToMapConverter;
import com.invoscan.invoscanv2.rules.InvoiceRule;
import com.invoscan.invoscanv2.rules.RuleResult;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ValidationService {

    @Autowired(required = false)
    private List<InvoiceRule> rules;

    public List<RuleResult> validate(Sheet sheet, FormulaEvaluator evaluator) {
        ExcelToMapConverter converter = new ExcelToMapConverter();
        Map<String, String> data = converter.extractFields(sheet, evaluator);

        List<RuleResult> results = new ArrayList<>();

        if (rules != null) {
            results.addAll(
                    rules.stream()
                            .map(rule -> rule.apply(data, sheet, evaluator))
                            .collect(Collectors.toList())
            );
        }

        return results;
    }
}
