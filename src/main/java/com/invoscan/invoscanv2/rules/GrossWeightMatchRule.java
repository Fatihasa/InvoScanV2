
package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import java.util.Map;


public class GrossWeightMatchRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        try {
            // N4 hücresindeki gross weight değeri (satır 3, sütun 13)
            double excelGrossWeight = sheet.getRow(3).getCell(13).getNumericCellValue();

            // Formdan gelen veri
            String userInput = data.get("kg");
            System.out.println("Formdan gelen gross weight: [" + userInput + "]");

            if (userInput == null || userInput.trim().isEmpty()) {
                return RuleResult.failed(getName(), "Gross weight input is missing from user.");
            }

            double userGrossWeight;
            try {
                userGrossWeight = Double.parseDouble(userInput.trim());
            } catch (NumberFormatException e) {
                return RuleResult.failed(getName(), "Gross weight must be a valid number.");
            }

            if (Math.abs(userGrossWeight - excelGrossWeight) < 0.01) {
                return RuleResult.passed(getName(), "Gross weight matches.");
            } else {
                return RuleResult.failed(getName(),
                        "Mismatch: User entered " + userGrossWeight + " kg, Excel contains " + excelGrossWeight + " kg.");
            }

        } catch (Exception e) {
            return RuleResult.failed(getName(), "Error during gross weight comparison: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Gross Weight Match Rule";
    }
}
