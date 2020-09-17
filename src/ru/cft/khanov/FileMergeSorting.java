package ru.cft.khanov;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileMergeSorting<T> {
    private final List<FileScanner<T>> fileScanners;
    private final String outputFile;
    private final Comparator<T> comparator;

    public FileMergeSorting(String[] inputFiles, String outputFile, ElementsType elementsType) throws FileNotFoundException {
        fileScanners = new ArrayList<>();

        for (String inputFile : inputFiles) {
            FileScanner<T> currentFileScanner = new FileScanner<>(inputFile);

            if (currentFileScanner.getElement() != null) {
                fileScanners.add(currentFileScanner);
            }
        }

        this.outputFile = outputFile;

        comparator = getComparator(elementsType);
    }

    private Comparator<T> getComparator(ElementsType elementsType) throws NumberFormatException {
        if (elementsType == ElementsType.INTEGER) {
            return (o1, o2) -> {
                int int1 = Integer.parseInt(o1.toString());
                int int2 = Integer.parseInt(o2.toString());

                return int1 - int2;
            };
        }

        if (elementsType == ElementsType.STRING) {
            return (o1, o2) -> {
                String s1 = (String) o1;
                String s2 = (String) o2;

                return s1.compareToIgnoreCase(s2);
            };
        }
        return null;
    }

    public void mergingFiles(OrderBy orderBy) {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            while (!fileScanners.isEmpty()) {
                T currentElement = fileScanners.get(0).getElement();
                int currentIndex = 0;

                int sign = 1;

                if (orderBy == OrderBy.DESC) {
                    sign = -1;
                }

                for (int i = 0; i < fileScanners.size(); i++) {
                    try {
                        if (comparator.compare(currentElement, fileScanners.get(i).getElement()) * sign > 0) {
                            currentElement = fileScanners.get(i).getElement();
                            currentIndex = i;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("invalid data format in file: " + fileScanners.get(i).getFileName());
                        return;
                    }
                }

                writer.println(currentElement);

                if (fileScanners.get(currentIndex).getScanner().hasNext()) {
                    fileScanners.get(currentIndex).setElement((T) fileScanners.get(currentIndex).getScanner().nextLine());
                } else {
                    fileScanners.get(currentIndex).getScanner().close();
                    fileScanners.remove(currentIndex);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found: " + e.getMessage());
        }
    }
}