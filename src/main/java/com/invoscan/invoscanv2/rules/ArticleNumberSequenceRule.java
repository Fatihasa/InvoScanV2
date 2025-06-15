package com.invoscan.invoscanv2.rules;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ArticleNumberSequenceRule implements InvoiceRule {

    @Override
    public RuleResult apply(Map<String, String> data, Sheet sheet, FormulaEvaluator evaluator) {
        int startRow = 19; // A20 → row index 19
        int col = 0; // A sütunu → index 0
        int expectedNumber = 1;

        while (true) {
            Row row = sheet.getRow(startRow);
            if (row == null) break;

            Cell cell = row.getCell(col);
            if (cell == null || cell.getCellType() == CellType.BLANK) break;

            int actual;
            try {
                if (cell.getCellType() == CellType.NUMERIC) {
                    actual = (int) cell.getNumericCellValue();
                } else {
                    actual = Integer.parseInt(cell.toString().trim());
                }
            } catch (Exception e) {
                return RuleResult.failed(getName(), "❌ Invalid number format at A" + (startRow + 1));
            }

            if (actual != expectedNumber) {
                return RuleResult.failed(getName(), "❌ Article No. mismatch at A" + (startRow + 1)
                        + ": expected " + expectedNumber + ", found " + actual);
            }

            expectedNumber++;
            startRow++;
        }

        return RuleResult.passed(getName(), "✅ Article numbers are sequential and correct.");
    }

    @Override
    public String getName() {
        return "Article Number Sequence Check";
    }
}
