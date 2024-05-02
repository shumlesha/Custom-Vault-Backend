package com.example.customvault.services.ecc;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class Point {
    private final BigInteger x;
    private final BigInteger y;

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
