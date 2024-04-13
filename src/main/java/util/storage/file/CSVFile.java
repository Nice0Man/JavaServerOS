package util.storage.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFile implements File {
    private final String filename;

    public CSVFile(String filename) {
        this.filename = filename;
    }

    @Override
    public List<String[]> read() throws IOException {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                data.add(fields);
            }
        }
        return data;
    }

    @Override
    public void write(List<String[]> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] record : data) {
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < record.length; i++) {
                    line.append(record[i]);
                    if (i < record.length - 1) {
                        line.append(",");
                    }
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    @Override
    public void create(String[] record) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < record.length; i++) {
                line.append(record[i]);
                if (i < record.length - 1) {
                    line.append(",");
                }
            }
            writer.write(line.toString());
            writer.newLine();
        }
    }

    @Override
    public void update(int rowIndex, String[] newData) throws IOException {
        List<String[]> data = read();
        if (rowIndex >= 0 && rowIndex < data.size()) {
            data.set(rowIndex, newData);
            write(data);
        } else {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
    }

    @Override
    public void delete(int rowIndex) throws IOException {
        List<String[]> data = read();
        if (rowIndex >= 0 && rowIndex < data.size()) {
            data.remove(rowIndex);
            write(data);
        } else {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
    }
}
