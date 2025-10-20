package org.jetbrains.kotlin.junit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoItemTest {

    @Test
    @DisplayName("Should create a new todo item with title and description")
    void shouldCreateTodoItem() {
        var item = new TodoItem("Buy groceries", "Milk, eggs, bread");

        assertNotNull(item.id());
        assertEquals("Buy groceries", item.title());
        assertEquals("Milk, eggs, bread", item.description());
        assertFalse(item.completed());
        assertNotNull(item.createdAt());
        assertNotNull(item.updatedAt());
    }

    @Test
    @DisplayName("Should generate unique IDs for different items")
    void shouldGenerateUniqueIds() {
        var item1 = new TodoItem("Task 1", "Description 1");
        var item2 = new TodoItem("Task 2", "Description 2");

        assertNotEquals(item1.id(), item2.id());
    }

    @Test
    @DisplayName("Should update title and updatedAt timestamp")
    void shouldUpdateTitle() {
        var item = new TodoItem("Old title", "Description");
        var originalUpdatedAt = item.updatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.setTitle("New title");

        assertEquals("New title", item.title());
        assertTrue(item.updatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    @DisplayName("Should update description and updatedAt timestamp")
    void shouldUpdateDescription() {
        var item = new TodoItem("Title", "Old description");
        var originalUpdatedAt = item.updatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.setDescription("New description");

        assertEquals("New description", item.description());
        assertTrue(item.updatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    @DisplayName("Should toggle completion status")
    void shouldToggleCompletion() {
        var item = new TodoItem("Task", "Description");

        assertFalse(item.completed());

        item.setCompleted(true);
        assertTrue(item.completed());

        item.setCompleted(false);
        assertFalse(item.completed());
    }

    @Test
    @DisplayName("Should update updatedAt when setting completion status")
    void shouldUpdateTimestampOnCompletion() {
        var item = new TodoItem("Task", "Description");
        var originalUpdatedAt = item.updatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        item.setCompleted(true);

        assertTrue(item.updatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    @DisplayName("Should format toString correctly for incomplete task")
    void shouldFormatToStringForIncompleteTask() {
        var item = new TodoItem("Buy milk", "From store");

        var result = item.toString();

        assertTrue(result.contains("[ ]"));
        assertTrue(result.contains("Buy milk"));
        assertTrue(result.contains("From store"));
    }

    @Test
    @DisplayName("Should format toString correctly for completed task")
    void shouldFormatToStringForCompletedTask() {
        var item = new TodoItem("Buy milk", "From store");
        item.setCompleted(true);

        var result = item.toString();

        assertTrue(result.contains("[X]"));
        assertTrue(result.contains("Buy milk"));
        assertTrue(result.contains("From store"));
    }

    @Test
    @DisplayName("Should compare items by ID for equality")
    void shouldCompareById() {
        var item1 = new TodoItem("Task", "Description");
        var item2 = new TodoItem("id", "Task", "Description", false,
                                      LocalDateTime.now(), LocalDateTime.now());

        assertNotEquals(item1, item2);

        var item3 = new TodoItem(item1.id(), "Different", "Different", true,
                                      LocalDateTime.now(), LocalDateTime.now());

        assertEquals(item1, item3);
        assertEquals(item1.hashCode(), item3.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null or different class")
    void shouldHandleNullAndDifferentClass() {
        var item = new TodoItem("Task", "Description");

        assertNotEquals(null, item);
        assertNotEquals("String", item);
    }

    @Test
    @DisplayName("Should maintain createdAt timestamp")
    void shouldMaintainCreatedAt() {
        var item = new TodoItem("Task", "Description");
        var createdAt = item.createdAt();

        item.setTitle("New title");
        item.setDescription("New description");
        item.setCompleted(true);

        assertEquals(createdAt, item.createdAt());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "A", "This is a very long title for a todo item"})
    @DisplayName("Should handle various title lengths")
    void shouldHandleVariousTitles(String title) {
        var item = new TodoItem(title, "Description");

        assertEquals(title, item.title());
    }
}
