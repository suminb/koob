package io.shortbread.koob.models;

public enum RecurringFrequency {
    None(0),
    Daily(1),
    Weekly(2),
    Monthly(3),
    Annually(4),
    Custom(5);

    private int value;

    RecurringFrequency(int value) {
        this.value = value;
    }
}
