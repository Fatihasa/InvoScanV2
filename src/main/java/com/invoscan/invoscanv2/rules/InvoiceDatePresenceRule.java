package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InvoiceDatePresenceRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        Row row = sheet.getRow(4); // J5 → satır index 4
        if (row == null) {
            return RuleResult.failed(getName(), "Row 5 does not exist.");
        }

        Cell cell = row.getCell(9); // J5 → sütun index 9
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return RuleResult.failed(getName(), "❌ Invoice Date cell (J5) is empty.");
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return RuleResult.passed(getName(), "✅ Invoice Date is present and formatted as a date.");
        }

        return RuleResult.passed(getName(), "✅ Invoice Date is present but not in date format.");
    }

    @Override
    public String getName() {
        return "Invoice Date Presence Check ";
    }
}
