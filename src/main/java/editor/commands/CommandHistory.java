package editor.commands;

import java.util.Stack;


public class CommandHistory {
    private final Stack<Command> history;

    public CommandHistory() {
        history = new Stack<>();
    }

    public void push(Command command) {
        history.push(command); // Добавляем команду в верхушку стека
    }

    public Command pop() {
        return history.pop(); // Извлекаем и возвращаем команду с верхушки стека
    }
}
