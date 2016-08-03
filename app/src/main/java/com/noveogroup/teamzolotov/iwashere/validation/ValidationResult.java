package com.noveogroup.teamzolotov.iwashere.validation;

import java.util.LinkedList;

public class ValidationResult {
    private LinkedList<ValidationProblem> validationProblems = new LinkedList<>();

    public ValidationResult() {

    }

    public void addProblem(ValidationProblem validationProblem) {
        validationProblems.add(validationProblem);
    }

    public LinkedList<ValidationProblem> getValidationProblems() {
        return validationProblems;
    }

    public boolean isNoProblems() {
        return validationProblems.size() == 0;
    }

    public enum ValidationProblem {
        SHORT_PASSWORD, INVALID_EMAIL, SHORT_USERNAME, NULL_REFERENCE, LONG_PASSWORD, LONG_USERNAME

    }

}
