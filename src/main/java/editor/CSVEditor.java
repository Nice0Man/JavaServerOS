package editor;

import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CSVEditor {
    private static String CSV_FILE_PATH = "src/main/resources/data.csv";
    private List<String[]> dataList;
    private String[] data;

    public void setDataList(List<String[]> dataList) throws IOException {
        if (!dataList.isEmpty()){
            this.dataList = new ArrayList<>(dataList);
        }
        saveToFile();
    }

    public CSVEditor() {
        dataList = new ArrayList<>();
        dataList.add(new String[]{"Data created at: ".concat(LocalDateTime.now().toString())});
    }

    public void createData(String[] newData) throws IOException {
        if (newData.length == 0){
            throw new IllegalArgumentException("Empty data passed!");
        }
        dataList.add(newData);
        saveToFile();
    }

    public void readData() throws IOException{
        try (Reader reader = new FileReader(CSV_FILE_PATH);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            dataList.clear();
            for (CSVRecord csvRecord : csvParser) {
                List<String> record = new ArrayList<>();
                for (String value : csvRecord) {
                    record.add(value);
                }
                dataList.add(record.toArray(new String[0]));
            }
        }
    }

    public void updateData(int rowIndex, String[] newData) throws IOException {
        if (rowIndex >= 1 && rowIndex < dataList.size()) {
            dataList.set(rowIndex, newData);
            saveToFile();
        } else if (rowIndex == 0){
            throw new IllegalArgumentException("Zero index is private for update operation!");
        } else {
            throw new IllegalArgumentException("Invalid row index for update operation!");
        }
    }

    public void deleteData(int rowIndex) throws IOException {
        if (rowIndex >= 1 && rowIndex < dataList.size()) {
            dataList.remove(rowIndex);
            saveToFile();
        } else if (rowIndex == 0){
            throw new IllegalArgumentException("Zero index is private for delete operation!");
        } else {
            throw new IllegalArgumentException("Invalid row index for delete operation!");
        }
    }

    public void get(int rowIndex) throws IOException{
        readData();
        if (rowIndex >= 0 && rowIndex < dataList.size()){
            data = dataList.get(rowIndex);
        } else {
            throw new IllegalArgumentException("Invalid row index for get operation!");
        }
    }

    private void saveToFile() throws IOException{
        try (Writer writer = new FileWriter(CSV_FILE_PATH);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            for (String[] record : dataList) {
                csvPrinter.printRecord((Object[]) record);
            }
        }
    }
}
