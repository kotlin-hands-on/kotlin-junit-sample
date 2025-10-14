package org.jetbrains.kotlin.junit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TodoRepository {
    private final List<TodoItem> todos = new ArrayList<>();

    public void add(TodoItem item) {
        todos.add(item);
    }

    public List<TodoItem> getAll() {
        return List.copyOf(todos);
    }

    public Optional<TodoItem> getById(String id) {
        return todos.stream()
            .filter(item -> item.id().equals(id))
            .findFirst();
    }

    public boolean remove(String id) {
        return todos.removeIf(item -> item.id().equals(id));
    }

    public void update(TodoItem item) {
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).id().equals(item.id())) {
                todos.set(i, item);
                return;
            }
        }
    }

    public int size() {
        return todos.size();
    }

    public void clear() {
        todos.clear();
    }
}
