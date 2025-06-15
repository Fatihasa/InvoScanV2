package com.invoscan.invoscanv2.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.invoscan.invoscanv2.model.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login"; // login.html Thymeleaf template
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam("email") String email,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference users = db.collection("users");
            ApiFuture<QuerySnapshot> query = users.whereEqualTo("email", email).get();
            List<QueryDocumentSnapshot> docs = query.get().getDocuments();

            if (docs.isEmpty()) {
                model.addAttribute("loginError", "User not found.");
                return "login";
            }

            User user = docs.get(0).toObject(User.class);

            // Parola kontrolü (BCrypt ile)
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                // Login başarılı
                session.setAttribute("user", user);
                return "redirect:/"; // Giriş sonrası ana sayfa
            } else {
                model.addAttribute("loginError", "Invalid password.");
                return "login";
            }
        } catch (InterruptedException | ExecutionException e) {
            model.addAttribute("loginError", "System error: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
