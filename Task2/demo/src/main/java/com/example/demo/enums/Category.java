package com.example.demo.enums;

public enum Category {
    TEMPERATURE("temperature"),
    LENGTH("length"),
    WEIGHT("weight"),
    TIME("time");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // This method is also needed
    public static Category fromString(String value) {
        for (Category category : Category.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}