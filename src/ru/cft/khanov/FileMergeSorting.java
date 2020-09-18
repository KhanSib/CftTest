package ru.cft.khanov;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class FileMergeSorting {
    private final List<FileScanner> fileScanners;
    private final String outputFile;
    private final Comparator<String> comparator;
    private final LinesType linesType;

    public FileMergeSorting(String[] inputFiles, String outputFile, LinesType linesType) throws FileNotFoundException {
        fileScanners = new ArrayList<>();

        for (String inputFile : inputFiles) {
            FileScanner currentFileScanner = new FileScanner(inputFile, linesType);

            if (currentFileScanner.getLine() != null) {
                fileScanners.add(currentFileScanner);
            }
        }

        this.outputFile = outputFile;

        comparator = getComparator(linesType);
        this.linesType = linesType;
    }

    private Comparator<String> getComparator(LinesType linesType) throws NumberFormatException {
        if (linesType == LinesType.INTEGER) {
            return (o1, o2) -> {
                int int1 = Integer.parseInt(o1);
                int int2 = Integer.parseInt(o2);

                return int1 - int2;
            };
        }

        if (linesType == LinesType.LONG) {
            return (o1, o2) -> {
                long long1 = Long.parseLong(o1);
                long long2 = Long.parseLong(o2);

                return Long.compare(long1, long2);
            };
        }

        if (linesType == LinesType.BIGINTEGER) {
            return (o1, o2) -> {
                BigInteger bigInteger1 = new BigInteger(o1);
                BigInteger bigInteger2 = new BigInteger(o2);

                return bigInteger1.compareTo(bigInteger2);
            };
        }

        if (linesType == LinesType.STRING) {
            return String::compareToIgnoreCase;
        }

        return null;
    }

    private static boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isAlphabetic(String str) {
        if (str == null || str.isEmpty()) return false;

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isAlphabetic(str.charAt(i))) return false;
        }
        return true;
    }

    protected static boolean isLineValid(String str, LinesType linesType) {
        if (linesType == LinesType.STRING && isAlphabetic(str)) {
            return true;
        }

        return linesType != LinesType.STRING && isNumber(str);
    }

    protected String getFirstValidLine(int currentIndex, Scanner currentFileScanner) {
        String nextLine = null;

        while (currentFileScanner.hasNext()) {
            nextLine = currentFileScanner.nextLine();

            if (linesType != LinesType.STRING && isNumber(nextLine)) {
                break;
            } else if (linesType != LinesType.STRING && !nextLine.isEmpty()) {
                System.out.println("invalid data format in file: " + fileScanners.get(currentIndex).getFileName()
                        + ", contents of line: " + nextLine);
            }

            if (linesType == LinesType.STRING && isAlphabetic(nextLine)) {
                break;
            } else if (linesType == LinesType.STRING && !nextLine.isEmpty()) {
                System.out.println("invalid data format in file: " + fileScanners.get(currentIndex).getFileName()
                        + ", contents of line: " + nextLine);
            }
        }

        return nextLine;
    }

    public void mergingFiles(OrderBy orderBy) {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            while (!fileScanners.isEmpty()) {
                String currentLine = fileScanners.get(0).getLine();
                int currentIndex = 0;

                int compareCoefficient = 1;

                if (orderBy == OrderBy.DESC) {
                    compareCoefficient = -1;
                }

                for (int i = 0; i < fileScanners.size(); i++) {
                    try {
                        if (comparator.compare(currentLine, fileScanners.get(i).getLine()) * compareCoefficient > 0) {
                            currentLine = fileScanners.get(i).getLine();
                            currentIndex = i;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("invalid data format in file: " + fileScanners.get(i).getFileName()
                                + ", contents of line: " + currentLine);
                        return;
                    }
                }

                writer.println(currentLine);

                Scanner currentFileScanner = fileScanners.get(currentIndex).getScanner();
                String nextLine = getFirstValidLine(currentIndex, currentFileScanner);

                if (nextLine != null && isLineValid(nextLine, linesType)) {
                    fileScanners.get(currentIndex).setLine(nextLine);
                } else {
                    currentFileScanner.close();
                    fileScanners.remove(currentIndex);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found: " + e.getMessage());
        }
    }
}