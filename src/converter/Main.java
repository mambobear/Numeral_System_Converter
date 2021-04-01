package converter;

import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.pow;

public class Main {
    public static void main(String[] args) {
        Input input = getInput();
        if (input == null) {
            System.out.println("error");
            return;
        }
        int fromRadix = input.getFromRadix();
        String xStr = input.getxStr();
        int toRadix = input.getToRadix();

        String result = "";

        if (fromRadix == toRadix) result = xStr;
        else {
            result = toBase10(xStr, fromRadix);
            if (toRadix != 10) result = fromBase10(result, toRadix);
        }
        System.out.println(result);
    }

    private static Input getInput() {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> inputList = new ArrayList<>();
        while (scanner.hasNext()) {
            inputList.add(scanner.next());
        }

        if (inputList.size() != 3) {
            return null;
        }

        String fromRadixStr = inputList.get(0);
        String xStr = inputList.get(1);
        String toRadixStr = inputList.get(2);

        int fromRadix;
        int toRadix;
        try {
            fromRadix = Integer.parseInt(fromRadixStr);
            toRadix = Integer.parseInt(toRadixStr);
        } catch (NumberFormatException e) {
            return null;
        }

        if (fromRadix < 1 || fromRadix > 36 || toRadix < 1 || toRadix > 36) {
            return null;
        }

        if (!valid(xStr, fromRadix)) {
            return null;
        }

        return new Input(fromRadix, toRadix, xStr);
    }

    private static boolean valid(String xStr, int fromRadix) {
        String regExpStr = "";
        if (fromRadix == 1) {
            regExpStr = String.format("1+");
        } else if (fromRadix <= 10) {
            char maxDigitChar = Character.forDigit(fromRadix - 1, fromRadix);
            regExpStr = String.format("[0-%c]+(.[0-%c]+)?", maxDigitChar, maxDigitChar);
        } else {
            char maxDigitChar = Character.forDigit(fromRadix - 1, fromRadix);
            regExpStr = String.format("[0-9a-%c]+(.[0-9a-%c]+)?", maxDigitChar, maxDigitChar);
        }

        return xStr.matches(regExpStr);
    }

    private static String toBase10(String xStr, int fromRadix) {
        if (fromRadix == 10) return xStr;
        if (fromRadix == 1) return Integer.toString(xStr.length());

        String[] parts = xStr.split("\\.");

        String yInt = convertIntToBase10(parts[0], fromRadix);

        String yFrac = "";
        if (parts.length == 2) {
            // TODO: fromRadix = 1?
            yFrac = convertFracToBase10(parts[1], fromRadix);
        }

        return yInt + yFrac;
    }

    private static String convertIntToBase10(String intStr, int fromRadix) {

        if (fromRadix == 10) return intStr;
        if (fromRadix == 1) return Integer.toString(intStr.length());

        long base10 = Long.parseLong(intStr, fromRadix);
        return Long.toString(base10);
    }

    private static String convertFracToBase10(String fracStr, int fromRadix) {
        assert fromRadix != 1;
        double result = 0;
        for (int i = 0; i < fracStr.length(); i++) {
            String digitStr = fracStr.substring(i, i + 1);
            result += Long.parseLong(digitStr, fromRadix) / pow(fromRadix, i + 1);
        }
        return Double.toString(result).substring(1);
    }

    private static String fromBase10(String xStr, int toRadix) {
        if (toRadix == 10) return xStr;

        double x = Double.parseDouble(xStr);
        int n = (int) x;
        double f = x - n;

        String yInt = convertIntFromBase10(n, toRadix);
        String yFrac = "";
        if (f != 0.0) {
            yFrac = convertFracFromBase10(f, toRadix);
        }

        return yInt + yFrac;
    }

    private static String convertIntFromBase10(int n, int toRadix) {
        if (toRadix == 1) {
            return "1".repeat(Math.max(0, n));
        }
        return Long.toString(n, toRadix);
    }

    private static String convertFracFromBase10(double f, int radixTo) {
        StringBuilder strb = new StringBuilder(".");

        int count = 0;
        int digit;
        while (count < 5) {
            f *= radixTo;
            digit = (int) (f);
            f -= digit;
            strb.append(Character.forDigit(digit, radixTo));
            count++;
        }
        return strb.toString();
    }
}

class Input {

    int fromRadix;
    int toRadix;
    String xStr;

    Input(int fromRadix, int toRadix, String xStr) {
        this.fromRadix = fromRadix;
        this.toRadix = toRadix;
        this.xStr = xStr;
    }

    public int getFromRadix() {
        return fromRadix;
    }

    public int getToRadix() {
        return toRadix;
    }

    public String getxStr() {
        return xStr;
    }
}