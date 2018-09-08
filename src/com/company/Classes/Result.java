package com.company.Classes;

class Result {
    public double accumulator; //Промежуточное значение
    public String remainder; // Остаток строки

    public Result() {
    }

    public Result(double accumulator, String remainder) {
        this.accumulator = accumulator;
        this.remainder = remainder;
    }
}