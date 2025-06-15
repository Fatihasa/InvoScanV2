package com.invoscan.invoscanv2.reader;

import com.invoscan.invoscanv2.rules.*;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.HashMap;
import java.util.Map;

public class ExcelToMapConverter {
    private final Map<String, FieldReader> readers = new HashMap<>();

    public ExcelToMapConverter() {
        readers.put("j4", new SimpleCellReader("J4"));
        readers.put("c5", new SimpleCellReader("C5"));
        readers.put("Street", new SimpleCellReader("C13"));
        readers.put("Postcode", new SimpleCellReader("C14"));
        readers.put("City", new SimpleCellReader("C15"));
        readers.put("Country", new SimpleCellReader("C16"));
        readers.put("currency", new SimpleCellReader("J13"));
        readers.put("Procedure Number", new SimpleCellReader("J16"));
        readers.put("Delivery Country", new SimpleCellReader("C17"));
        readers.put("Consignee VAT", new SimpleCellReader("J11"));
        readers.put("Delivery City", new SimpleCellReader("J15"));
    }

    public Map<String, String> extractFields(Sheet sheet, FormulaEvaluator evaluator) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, FieldReader> entry : readers.entrySet()) {
            result.put(entry.getKey(), entry.getValue().read(sheet, evaluator));
        }
        return result;
    }
}
