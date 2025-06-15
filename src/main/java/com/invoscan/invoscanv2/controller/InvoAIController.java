package com.invoscan.invoscanv2.controller;

import com.invoscan.invoscanv2.rules.HSCodeSuggestionRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InvoAIController {

    @Autowired
    private HSCodeSuggestionRule hsCodeSuggestionRule;

    @GetMapping("/invoai")
    public String showInvoaiPage() {
        return "invoai"; // Formu gösterir
    }

    @PostMapping("/invoai")
    public String askHsCode(@RequestParam("description") String description, Model model) {
        String response = hsCodeSuggestionRule.ask(description); // özel prompt'la çağır
        model.addAttribute("description", description);
        model.addAttribute("response", response);
        return "invoai";
    }
}
