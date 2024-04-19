package editor.commands;

import editor.CSVApp;
import editor.CSVEditor;

import java.util.*;

// Абстрактный класс Command определяет общий интерфейс для всех конкретных команд.
public abstract class Command {
    protected CSVApp csvApp;
    protected CSVEditor csvEditor;
    protected List<String[]> backup = new ArrayList<>(); // Резервная копия данных CSV

    public Command(CSVApp csvApp, CSVEditor csvEditor) {
        this.csvApp = csvApp;
        this.csvEditor = csvEditor;
    }

    // Сохранение текущих данных CSV в качестве резервной копии
    public void saveBackup() {
        if (!csvEditor.getDataList().isEmpty()){
            backup = new ArrayList<>(csvEditor.getDataList());
        }
    }

    // Отмена операции и восстановление резервной копии данных CSV
    public void undo(){
        csvEditor.setDataList(backup);
    }

    // Выполнение команды
    public abstract boolean execute();
}

