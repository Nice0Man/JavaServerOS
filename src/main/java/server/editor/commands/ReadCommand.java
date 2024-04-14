package server.editor.commands;


// Конкретная команда для чтения данных из CSV файла
class ReadCommand extends Command {
    public ReadCommand(CSVApp csvApp, CSVEditor activeEditor) {
        super(csvApp, activeEditor);
    }

    @Override
    public boolean execute() {
        csvEditor.readData();
        return false;
    }
}
