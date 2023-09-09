package Validation;

public class isPassValid {

    public boolean isValidPass(String password) {
        // Check if the password is at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Check if the password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check if the password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check if the password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check if the password contains at least one special character (e.g., !, @, #, $, etc.)
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }

        // If all checks pass, the password is valid
        return true;
    }

}
