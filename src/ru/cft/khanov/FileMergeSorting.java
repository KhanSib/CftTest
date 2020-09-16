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

    private Comparator<T> getComparator(ElementsType elementsType) {
        if (elementsType == ElementsType.INTEGER) {
            return (Comparator<T>) Comparator.comparingInt(Object::hashCode);
        }

        if (elementsType == ElementsType.STRING) {
            return (Comparator<T>) Comparator.naturalOrder();
        }

        return null;
    }

    public void mergingFiles(OrderBy orderBy) {
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            while (!fileScanners.isEmpty()) {
                T currentElement = fileScanners.get(0).getElement();
                int currentIndex = 0;

                int coefficient = 1;

                if (orderBy == OrderBy.DESC) {
                    coefficient = -1;
                }

                for (int i = 0; i < fileScanners.size(); i++) {
                    if (comparator.compare(currentElement, fileScanners.get(i).getElement()) * coefficient > 0) {
                        currentElement = fileScanners.get(i).getElement();
                        currentIndex = i;
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
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
