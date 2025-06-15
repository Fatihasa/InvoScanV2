package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Map;

public interface InvoiceRule {
    RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator);
    String getName();
}
