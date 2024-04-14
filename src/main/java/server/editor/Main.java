package server.editor;

import server.editor.commands.CommandHistory;

public class Main {
    public static void main(String[] args) {
        // Создаем экземпляр приложения и инициализируем редактор CSV
        CSVApp app = new CSVApp();
        CSVEditor editor = new CSVEditor();
        app.setActiveEditor(editor);

        CommandHistory history = new CommandHistory();
        app.setHistory(history);

        // Выполняем несколько операций CRUD с файлом CSV

        app.create(new String[]{"Hello, World!"});
        app.create(new String[]{"Test, Test!"});
        app.read();
        app.print();

        app.update(1, new String[]{"#1", "Updated row"});
        app.delete(2);
        app.read();
        app.print();
        // Отменяем последнюю операцию
        app.undo();

        // Снова считываем данные и выводим их на экран
        app.read();
        app.print();
    }
}
