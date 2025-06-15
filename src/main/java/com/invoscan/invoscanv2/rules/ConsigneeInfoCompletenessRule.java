package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsigneeInfoCompletenessRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        int[] cRows = {10, 11, 12, 13, 14, 15, 16}; // C11–C17 → satır indexleri
        int[] jRows = {10, 11, 12, 13, 14, 15, 16}; // J11–J17 → aynı satırlar
        int colC = 2; // C sütunu
        int colJ = 9; // J sütunu

        for (int rowIndex : cRows) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return RuleResult.failed(getName(), "❌ Consignee info missing: Row " + (rowIndex + 1) + " is empty.");
            }
            Cell cell = row.getCell(colC);
            if (cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
                return RuleResult.failed(getName(), "❌ Consignee info missing: Cell C" + (rowIndex + 1) + " is empty.");
            }
        }

        for (int rowIndex : jRows) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return RuleResult.failed(getName(), "❌ Consignee info missing: Row " + (rowIndex + 1) + " is empty.");
            }
            Cell cell = row.getCell(colJ);
            if (cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
                return RuleResult.failed(getName(), "❌ Consignee info missing: Cell J" + (rowIndex + 1) + " is empty.");
            }
        }

        return RuleResult.passed(getName(), "✅ All consignee information cells are filled.");
    }

    @Override
    public String getName() {
        return "Consignee Info Completeness Check";
    }
}
