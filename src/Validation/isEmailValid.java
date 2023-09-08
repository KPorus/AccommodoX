package Validation;

public class isEmailValid {

    public boolean isValidEmail(String email) {
        // Regular expression pattern for a simple email format validation
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
}
