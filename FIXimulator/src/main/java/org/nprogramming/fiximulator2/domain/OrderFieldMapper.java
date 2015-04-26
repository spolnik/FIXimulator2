package org.nprogramming.fiximulator2.domain;

public final class OrderFieldMapper {

    private OrderFieldMapper() {
    }

    public static String expandSide(char side) {

        switch (side) {
            case '1':
                return "Buy";
            case '2':
                return "Sell";
            case '3':
                return "Buy minus";
            case '4':
                return "Sell plus";
            case '5':
                return "Sell short";
            case '6':
                return "Sell short exempt";
            case '7':
                return "Undisclosed";
            case '8':
                return "Cross";
            case '9':
                return "Cross short";
            default:
                return "<UNKNOWN>";
        }
    }
}

