package ru.cft.khanov;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileScanner {
    private final String fileName;
    private String line;
    private final Scanner scanner;

    public FileScanner(String fileName, LinesType linesType) throws FileNotFoundException {
        this.fileName = fileName;

        Scanner scanner = new Scanner(new FileInputStream(fileName));
        this.scanner = scanner;

        String nextLine = null;

        while (scanner.hasNext()) {
            nextLine = scanner.nextLine();

            if (!nextLine.isEmpty() && FileMergeSorting.isLineValid(nextLine, linesType)) {
                break;
            }

            if (!nextLine.isEmpty()) {
                System.out.println("invalid data format in file: " + fileName
                        + ", contents of line: " + nextLine);
            }
        }

        if (nextLine != null) {
            line = nextLine;
        }
    }

    public String getFileName() {
        return fileName;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public Scanner getScanner() {
        return scanner;
    }
}