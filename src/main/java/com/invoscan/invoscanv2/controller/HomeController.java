package com.invoscan.invoscanv2.controller;

import com.invoscan.invoscanv2.ValidationService;
import com.invoscan.invoscanv2.rules.GrossWeightMatchRule;
import com.invoscan.invoscanv2.rules.RuleResult;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private ValidationService validationService;

    // Yardımcı method: mesajdan sadece ilk cümleyi alır
    private String firstSentence(String text) {
        int dot = text.indexOf(".");
        if (dot > 0) return text.substring(0, dot + 1).trim();
        return text.trim();
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        // --- LOGIN KONTROLÜ ---
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // ----------------------

        @SuppressWarnings("unchecked")
        Map<String, List<RuleResult>> resultMap =
                (Map<String, List<RuleResult>>) session.getAttribute("resultMap");
        if (resultMap == null) resultMap = new LinkedHashMap<>();

        // --- Profesyonel toplu müşteri mesajı ---
        StringBuilder errorBuilder = new StringBuilder();
        boolean hasAnyFail = false;
        errorBuilder.append("Dear Customer,\n\n");
        errorBuilder.append("During our invoice document validation, we found the following errors:\n\n");
        for (var entry : resultMap.entrySet()) {
            String file = entry.getKey();
            for (var res : entry.getValue()) {
                if (!res.isPassed()) {
                    hasAnyFail = true;
                    errorBuilder.append("- [").append(file).append("] ")
                            .append(res.getName()).append(": ")
                            .append(firstSentence(res.getMessage())).append("\n");
                }
            }
        }
        errorBuilder.append("\nPlease review these issues and make the necessary corrections before resubmission.\n\n");
        errorBuilder.append("Best regards,\nInvoScan Validation Team");
        // ----------------------------------------

        model.addAttribute("filenames", resultMap.keySet());
        model.addAttribute("allResults", resultMap);
        model.addAttribute("allErrorsText", errorBuilder.toString().trim());
        model.addAttribute("hasAnyFail", hasAnyFail);

        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("files") MultipartFile[] files,
                         @RequestParam(value = "kg", required = false) String grossWeight,
                         HttpSession session) {
        // --- LOGIN KONTROLÜ ---
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // ----------------------

        @SuppressWarnings("unchecked")
        Map<String, List<RuleResult>> resultMap =
                (Map<String, List<RuleResult>>) session.getAttribute("resultMap");
        if (resultMap == null) resultMap = new LinkedHashMap<>();

        for (MultipartFile file : files) {
            try (InputStream is = file.getInputStream(); Workbook wb = new XSSFWorkbook(is)) {
                Sheet sheet = wb.getSheetAt(0);
                FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

                List<RuleResult> results = validationService.validate(sheet, evaluator);

                Map<String, String> data = Map.of("kg", grossWeight);
                GrossWeightMatchRule rule = new GrossWeightMatchRule();
                RuleResult extraResult = rule.apply(data, sheet, evaluator);
                results.add(extraResult);

                resultMap.put(file.getOriginalFilename(), results);

            } catch (Exception e) {
                resultMap.put(file.getOriginalFilename(), List.of(
                        RuleResult.failed("System Error", e.getMessage())
                ));
            }
        }

        session.setAttribute("resultMap", resultMap);
        return "redirect:/";
    }

    @GetMapping("/result/{filename}")
    public String result(@PathVariable String filename, Model model, HttpSession session) {
        // --- LOGIN KONTROLÜ ---
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // ----------------------

        @SuppressWarnings("unchecked")
        Map<String, List<RuleResult>> resultMap =
                (Map<String, List<RuleResult>>) session.getAttribute("resultMap");
        if (resultMap != null && resultMap.containsKey(filename)) {
            model.addAttribute("results", resultMap.get(filename));
            model.addAttribute("selectedFile", filename);
        }

        // --- Profesyonel toplu müşteri mesajı ---
        StringBuilder errorBuilder = new StringBuilder();
        boolean hasAnyFail = false;
        errorBuilder.append("Dear Customer,\n\n");
        errorBuilder.append("During our invoice document validation, we found the following errors:\n\n");
        if (resultMap != null) {
            for (var entry : resultMap.entrySet()) {
                String file = entry.getKey();
                for (var res : entry.getValue()) {
                    if (!res.isPassed()) {
                        hasAnyFail = true;
                        errorBuilder.append("- [").append(file).append("] ")
                                .append(res.getName()).append(": ")
                                .append(firstSentence(res.getMessage())).append("\n");
                    }
                }
            }
        }
        errorBuilder.append("\nPlease review these issues and make the necessary corrections before resubmission.\n\n");
        errorBuilder.append("Best regards,\nInvoScan Validation Team");
        // ----------------------------------------

        model.addAttribute("filenames", resultMap != null ? resultMap.keySet() : List.of());
        model.addAttribute("allResults", resultMap);
        model.addAttribute("allErrorsText", errorBuilder.toString().trim());
        model.addAttribute("hasAnyFail", hasAnyFail);

        return "index";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("filename") String filename, HttpSession session) {
        // --- LOGIN KONTROLÜ ---
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // ----------------------

        @SuppressWarnings("unchecked")
        Map<String, List<RuleResult>> resultMap =
                (Map<String, List<RuleResult>>) session.getAttribute("resultMap");
        if (resultMap != null) {
            resultMap.remove(filename);
            session.setAttribute("resultMap", resultMap);
        }
        return "redirect:/";
    }

    @GetMapping("/clear")
    public String clearAll(HttpSession session) {
        // --- LOGIN KONTROLÜ ---
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        // ----------------------

        session.removeAttribute("resultMap");
        return "redirect:/";
    }
}
