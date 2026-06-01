package org.example.handlers.gui.entity.enum_0;

public enum Bits {
    W(1 << 0), S(1 << 2), A(1 << 3), D(1 << 4);
    private int key;
    Bits(int key) {
        this.key = key;
    }
    public int getKey() {
        return key;
    }
}