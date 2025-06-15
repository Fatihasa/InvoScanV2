package com.invoscan.invoscanv2.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.invoscan.invoscanv2.model.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserAdminController {

    // Admin için: http://localhost:8080/add-user?email=admin@invoscan.com&password=123456
    @GetMapping("/add-user")
    @ResponseBody
    public String addUser(@RequestParam String email, @RequestParam String password) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection("users");

        // Şifreyi hashle
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(null, email, hash);

        ApiFuture<DocumentReference> result = users.add(user);

        return "User added: " + email + "<br>ID: " + result.get().getId();
    }
}
