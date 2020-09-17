package ru.cft.khanov;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileScanner<T> {
    private final String fileName;
    private T element;
    private final Scanner scanner;

    public FileScanner(String fileName) throws FileNotFoundException {
        this.fileName = fileName;

        Scanner scanner = new Scanner(new FileInputStream(fileName));
        this.scanner = scanner;

        if (scanner.hasNext()) {
            element = (T) scanner.nextLine();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public Scanner getScanner() {
        return scanner;
    }
}