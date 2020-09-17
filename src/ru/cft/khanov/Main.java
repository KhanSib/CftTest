package ru.cft.khanov;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    private static void help() {
        System.out.println("First argument: sort mode -a ascending," +
                " -d descending, optional, default sorted by ascending;");
        System.out.println("Second argument: data type -s text, -i number, is necessary;");
        System.out.println("Third argument: output file name, is necessary;");
        System.out.println("Subsequent arguments: input file names, at least one;");
        System.out.println("Example: main -d -s out.txt in1.txt in2.txt");
    }

    public static void main(String[] args) {
        // args = new String[]{"-d", "-s", "out.txt", "in1.txt", "in2.txt"};

        if (args.length < 3) {
            help();
            throw new IllegalArgumentException("Not enough command line arguments.");
        }

        ElementsType elementsType = ElementsType.INTEGER;
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
            if (args1.equals("-s") || args1.equals("-i")) {
                if (args1.equals("-s")) {
                    elementsType = ElementsType.STRING;
                }
            } else {
                help();
                throw new IllegalArgumentException("Not valid command line arguments.");
            }
        } else {
            if (args0.equals("-s") || args0.equals("-i")) {
                if (args0.equals("-s")) {
                    elementsType = ElementsType.STRING;
                }
            } else {
                help();
                throw new IllegalArgumentException("Not valid command line arguments.");
            }
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
            FileMergeSorting<String> fileMergeSorting = new FileMergeSorting<>(files, outFile, elementsType);
            fileMergeSorting.mergingFiles(orderBy);
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        }
    }
}