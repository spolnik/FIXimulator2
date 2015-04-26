package org.nprogramming.fiximulator2.domain;

public final class ExecutionFieldMapper {

    private ExecutionFieldMapper() {

    }

    public static String expandExecType(char execType) {
        switch (execType) {
            case '0':
                return "New";
            case '1':
                return "Partial fill";
            case '2':
                return "Fill";
            case '3':
                return "Done for day";
            case '4':
                return "Canceled";
            case '5':
                return "Replace";
            case '6':
                return "Pending Cancel";
            case '7':
                return "Stopped";
            case '8':
                return "Rejected";
            case '9':
                return "Suspended";
            case 'A':
                return "Pending New";
            case 'B':
                return "Calculated";
            case 'C':
                return "Expired";
            case 'D':
                return "Restated";
            case 'E':
                return "Pending Replace";
            default:
                return "<UNKNOWN>";
        }
    }
}
