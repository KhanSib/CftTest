package ru.cft.khanov;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //args = new String[]{"-d", "-i", "out.txt", "in1.txt", "in2.txt"};

        if (args.length < 3) {
            help();
            throw new IllegalArgumentException("Not enough command line arguments.");
        }

        LinesType linesType = LinesType.INTEGER;
        OrderBy orderBy = OrderBy.ASC;

        String args0 = args[0];

        boolean hasOrderByArgument = true;

        if (args0.equals("-a") || args0.equals("-d")) {
            if (args0.equals("-d")) {
                orderBy = OrderBy.DESC;
            }
        } else {
            hasOrderByArgument = false;
        }

        int argsLength = args.length;

        if (hasOrderByArgument && argsLength < 4) {
            help();
            throw new IllegalArgumentException("Not enough command line arguments.");
        }

        String args1 = args[1];

        if (hasOrderByArgument) {
            linesType = getLinesType(linesType, args1);
        } else {
            linesType = getLinesType(linesType, args0);
        }

        String outFile;
        String[] files = new String[]{};

        if (hasOrderByArgument) {
            outFile = args[2];
            files = Arrays.copyOf(files, argsLength - 3);
            System.arraycopy(args, 3, files, 0, argsLength - 3);
        } else {
            outFile = args[1];
            files = Arrays.copyOf(files, argsLength - 2);
            System.arraycopy(args, 2, files, 0, argsLength - 2);
        }

        try {
            FileMergeSorting fileMergeSorting = new FileMergeSorting(files, outFile, linesType);
            fileMergeSorting.mergingFiles(orderBy);
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        }
    }

    private static void help() {
        System.out.println("First argument: sort mode -a ascending," +
                " -d descending, optional, default sorted by ascending;");
        System.out.println("Second argument: data type -s text, -i int number, " +
                "-l long number, -b bigInteger number, is necessary;");
        System.out.println("Third argument: output file name, is necessary;");
        System.out.println("Subsequent arguments: input file names, at least one;");
        System.out.println("Example: main -d -s out.txt in1.txt in2.txt");
    }

    private static LinesType getLinesType(LinesType linesType, String arg) {
        if (arg.equals("-s") || arg.equals("-l") || arg.equals("-b") || arg.equals("-i")) {
            if (arg.equals("-s")) {
                linesType = LinesType.STRING;
            }

            if (arg.equals("-l")) {
                linesType = LinesType.LONG;
            }

            if (arg.equals("-b")) {
                linesType = LinesType.BIGINTEGER;
            }
        } else {
            help();
            throw new IllegalArgumentException("Not valid command line arguments.");
        }

        return linesType;
    }
}