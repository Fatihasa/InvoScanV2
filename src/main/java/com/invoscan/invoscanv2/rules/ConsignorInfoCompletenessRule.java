package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsignorInfoCompletenessRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        int[] rows = {3, 5, 6, 7, 8}; // C4, C6, C7, C8, C9 → satır indexleri
        int col = 2; // C sütunu → index 2

        for (int rowIndex : rows) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return RuleResult.failed(getName(), "❌ Consignor info missing: Row " + (rowIndex + 1) + " is empty.");
            }

            Cell cell = row.getCell(col);
            if (cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
                return RuleResult.failed(getName(), "❌ Consignor info missing: Cell C" + (rowIndex + 1) + " is empty.");
            }
        }

        return RuleResult.passed(getName(), "✅ All consignor information cells are filled.");
    }

    @Override
    public String getName() {
        return "Consignor Info Completeness Check";
    }
}
