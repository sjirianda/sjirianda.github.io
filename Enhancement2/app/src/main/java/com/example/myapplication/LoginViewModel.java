package com.example.myapplication;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

// ✅ Missing imports added

import com.example.myapplication.WeightBST;
import com.example.myapplication.WeightEntry;

public class LoginViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final WeightBST weightTree = new WeightBST(); // ➕ Added BST support

    public LoginViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
    }

    // 🔐 Existing login features
    public boolean validateCredentials(String inputUser, String inputPass) {
        return inputUser.equals(userRepository.getUsername()) &&
                inputPass.equals(userRepository.getPassword());
    }

    public void registerUser(String username, String password) {
        userRepository.saveUser(username, password);
    }

    public String getStoredUsername() {
        return userRepository.getUsername();
    }

    public boolean isUserLoggedIn() {
        return getStoredUsername() != null;
    }

    // 🌲 New BST functions
    public void addWeightEntry(String date, float weight) {
        weightTree.insert(new WeightEntry(date, weight));
    }

    public WeightEntry searchExactWeight(float weight) {
        return weightTree.search(weight);
    }

    public WeightEntry searchClosestWeight(float targetWeight) {
        return weightTree.findClosest(targetWeight);
    }
}
