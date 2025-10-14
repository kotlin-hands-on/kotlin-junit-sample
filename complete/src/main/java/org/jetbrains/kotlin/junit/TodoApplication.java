package org.jetbrains.kotlin.junit;

import jexer.TAction;
import jexer.TApplication;
import jexer.TList;
import jexer.TMessageBox;
import jexer.TWindow;

import java.util.List;

public final class TodoApplication extends TApplication {
    private final TodoRepository repository;
    private TList todoList;
    private TWindow mainWindow;
    private List<TodoItem> currentItems;

    public TodoApplication() throws Exception {
        super(BackendType.SWING);
        this.repository = new TodoRepository();
        this.currentItems = List.of();

        addToolMenu();
        addFileMenu();

        createMainWindow();
        loadTodos();
    }

    private void createMainWindow() {
        mainWindow = addWindow("Todo List", 0, 0, 80, 24, TWindow.CENTERED | TWindow.RESIZABLE);

        mainWindow.addLabel("My Todos:", 2, 2);

        todoList = mainWindow.addList(List.of(), 2, 4, 60, 12, new TAction() {
            public void DO() { showTodoDetails(); }
        });

        mainWindow.addButton("&Add Todo", 2, 17, new TAction() {
            public void DO() { addTodo(); }
        });
        mainWindow.addButton("&Toggle Complete", 15, 17, new TAction() {
            public void DO() { toggleComplete(); }
        });
        mainWindow.addButton("&Delete", 35, 17, new TAction() {
            public void DO() { deleteTodo(); }
        });
        mainWindow.addButton("&Refresh", 45, 17, new TAction() {
            public void DO() { loadTodos(); }
        });
        mainWindow.addButton("E&xit", 57, 17, new TAction() {
            public void DO() { exitApplication(); }
        });

        mainWindow.addLabel("Press Enter on a todo to view details", 2, 20);
    }

    private void loadTodos() {
        currentItems = repository.getAll();
        var displayItems = currentItems.stream()
            .map(TodoItem::toString)
            .toList();

        todoList.setList(displayItems);
    }

    private void addTodo() {
        var title = inputBox("Add Todo", "Enter todo title:", "").getText();
        if (title != null && !title.isBlank()) {
            var description = inputBox("Add Todo", "Enter description (optional):", "").getText();

            var newItem = new TodoItem(title.trim(), description != null ? description.trim() : "");
            repository.add(newItem);
            loadTodos();

            messageBox("Success", "Todo added successfully!");
        }
    }

    private void toggleComplete() {
        var selected = todoList.getSelectedIndex();
        if (selected >= 0 && selected < currentItems.size()) {
            var item = currentItems.get(selected);
            item.setCompleted(!item.completed());
            repository.update(item);
            loadTodos();
            todoList.setSelectedIndex(selected);
        }
    }

    private void deleteTodo() {
        var selected = todoList.getSelectedIndex();
        if (selected >= 0 && selected < currentItems.size()) {
            var item = currentItems.get(selected);

            if (messageBox("Confirm Delete",
                          "Delete todo: " + item.title() + "?",
                          TMessageBox.Type.YESNO).isYes()) {
                repository.remove(item.id());
                loadTodos();
            }
        }
    }

    private void showTodoDetails() {
        var selected = todoList.getSelectedIndex();
        if (selected >= 0 && selected < currentItems.size()) {
            var item = currentItems.get(selected);

            var detailWindow = addWindow("Todo Details", 0, 0, 60, 15, TWindow.CENTERED | TWindow.MODAL);

            var row = 2;
            detailWindow.addLabel("Title:", 2, row++);
            detailWindow.addLabel(item.title(), 4, row++);
            row++;

            detailWindow.addLabel("Description:", 2, row++);
            detailWindow.addLabel(item.description().isEmpty() ? "(none)" : item.description(), 4, row++);
            row++;

            detailWindow.addLabel("Status:", 2, row++);
            detailWindow.addLabel(item.completed() ? "Completed" : "Not completed", 4, row++);
            row++;

            detailWindow.addButton("&Close", 25, row, new TAction() {
                public void DO() { detailWindow.close(); }
            });
        }
    }

    private void exitApplication() {
        if (messageBox("Confirm Exit", "Exit the application?", TMessageBox.Type.YESNO).isYes()) {
            super.exit();
        }
    }

    public TodoRepository getRepository() {
        return repository;
    }

    public static void main(String[] args) {
        try {
            TodoApplication app = new TodoApplication();
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
