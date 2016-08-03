package com.noveogroup.teamzolotov.iwashere.util;

import android.content.Context;

import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.validation.ValidationResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidatorUtils {
    private static final int MIN_LENGTH_OF_PASSWORD = 6;
    private static final int MAX_LENGTH_OF_PASSWORD = 256;
    private static final int MIN_LENGTH_OF_USERNAME = 2;
    private static final int MAX_LENGTH_OF_USERNAME = 256;

    private ValidatorUtils() {
        throw new UnsupportedOperationException("Trying to create instance of utility class");
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static void validatePassword(String password, ValidationResult validationResult) {
        if (null == password) {
            validationResult.addProblem(ValidationResult.ValidationProblem.NULL_REFERENCE);
            return;
        }

        if (password.length() < MIN_LENGTH_OF_PASSWORD) {
            validationResult.addProblem(ValidationResult.ValidationProblem.SHORT_PASSWORD);
            return;
        }

        if (password.length() > MAX_LENGTH_OF_PASSWORD) {
            validationResult.addProblem(ValidationResult.ValidationProblem.LONG_PASSWORD);
        }
    }

    public static void validateUsername(String username, ValidationResult validationResult) {
        if (null == username) {
            validationResult.addProblem(ValidationResult.ValidationProblem.NULL_REFERENCE);
            return;
        }

        if (username.length() < MIN_LENGTH_OF_USERNAME) {
            validationResult.addProblem(ValidationResult.ValidationProblem.SHORT_USERNAME);
            return;
        }

        if (username.length() > MAX_LENGTH_OF_USERNAME) {
            validationResult.addProblem(ValidationResult.ValidationProblem.LONG_USERNAME);
        }
    }

    public static void validateEmail(String email, ValidationResult validationResult) {
        if (!validateEmail(email)) {
            validationResult.addProblem(ValidationResult.ValidationProblem.INVALID_EMAIL);
        }
    }

    public static String getMessage(Context context, ValidationResult.ValidationProblem validationProblem) {
        String message = null;

        switch(validationProblem) {
            case NULL_REFERENCE:
                message = context.getString(R.string.unexpected_error_message);
                break;

            case SHORT_PASSWORD:
                message = context.getString(R.string.short_password_error_message);
                break;

            case LONG_PASSWORD:
                message = context.getString(R.string.long_password_error_message);
                break;

            case SHORT_USERNAME:
                message = context.getString(R.string.short_username_error_message);
                break;

            case LONG_USERNAME:
                message = context.getString(R.string.long_username_error_message);
                break;
            case INVALID_EMAIL:
                message = context.getString(R.string.invalid_email_error_message);
        }

        return message;
    }
}
