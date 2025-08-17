package com.example.myapplication;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    public LoginViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application.getApplicationContext());
    }

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
}
