package com.Fuad.BankApplicationSimulation.Util;

import com.Fuad.BankApplicationSimulation.Enums.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {

    private static final BigDecimal AZN_TO_USD = BigDecimal.valueOf(0.59);
    private static final BigDecimal EUR_TO_USD = BigDecimal.valueOf(1.1);

    private static final BigDecimal USD_TO_AZN = BigDecimal.ONE.divide(AZN_TO_USD, 6, RoundingMode.HALF_UP);
    private static final BigDecimal USD_TO_EUR = BigDecimal.ONE.divide(EUR_TO_USD, 6, RoundingMode.HALF_UP);

    public static BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (from == to) return amount.setScale(2, RoundingMode.HALF_UP);

        //Vse delaem v USD a posle v nujnuyu nam valyutu tobiw sleduyuwiy metod
        BigDecimal amountInUSD;
        switch (from) {
            case USD -> amountInUSD = amount;
            case AZN -> amountInUSD = amount.multiply(AZN_TO_USD);
            case EUR -> amountInUSD = amount.multiply(EUR_TO_USD);
            default -> throw new IllegalArgumentException("Unsupported currency: " + from);
        }

        // etot
        BigDecimal result;
        switch (to) {
            case USD -> result = amountInUSD;
            case AZN -> result = amountInUSD.multiply(USD_TO_AZN);
            case EUR -> result = amountInUSD.multiply(USD_TO_EUR);
            default -> throw new IllegalArgumentException("Unsupported currency: " + to);
        }

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
