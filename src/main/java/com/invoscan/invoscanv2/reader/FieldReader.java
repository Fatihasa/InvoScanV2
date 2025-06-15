package com.invoscan.invoscanv2.reader;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

public interface FieldReader {
    String read(Sheet sheet, FormulaEvaluator evaluator);
}
