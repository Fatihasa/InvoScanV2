package com.invoscan.invoscanv2.rules;

public class RuleResult {
    private final String name;
    private final boolean passed;
    private final String message;

    private RuleResult(String name, boolean passed, String message) {
        this.name = name;
        this.passed = passed;
        this.message = message;
    }

    public static RuleResult passed(String name, String message) {
        return new RuleResult(name, true, message);
    }

    public static RuleResult failed(String name, String message) {
        return new RuleResult(name, false, message);
    }

    public boolean isPassed() {
        return passed;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
