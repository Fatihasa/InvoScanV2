package com.invoscan.invoscanv2.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

public class SimpleCellReader implements FieldReader {
    private final String cellRef;

    public SimpleCellReader(String cellRef) {
        this.cellRef = cellRef;
    }

    @Override
    public String read(Sheet sheet, FormulaEvaluator evaluator) {
        CellReference ref = new CellReference(cellRef);
        Row row = sheet.getRow(ref.getRow());
        if (row == null) return "";
        Cell cell = row.getCell(ref.getCol());
        if (cell == null) return "";

        CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null) return "";

        return switch (cellValue.getCellType()) {
            case STRING -> cellValue.getStringValue().trim();
            case NUMERIC -> {
                double raw = cellValue.getNumberValue();
                long longVal = (long) raw;
                yield (raw == longVal) ? String.valueOf(longVal) : String.valueOf(raw);
            }
            case BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
            default -> "";
        };
    }
}
