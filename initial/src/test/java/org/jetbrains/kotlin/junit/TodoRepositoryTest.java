package org.jetbrains.kotlin.junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class TodoRepositoryTest {

    private TodoRepository repository;
    private TodoItem testItem1;
    private TodoItem testItem2;

    @BeforeEach
    void setUp() {
        repository = new TodoRepository();
        testItem1 = new TodoItem("Task 1", "Description 1");
        testItem2 = new TodoItem("Task 2", "Description 2");
    }

    @Test
    @DisplayName("Should start with empty repository")
    void shouldStartEmpty() {
        assertEquals(0, repository.size());
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    @DisplayName("Should add item to repository")
    void shouldAddItem() {
        repository.add(testItem1);

        assertEquals(1, repository.size());
        assertTrue(repository.getAll().contains(testItem1));
    }

    @Test
    @DisplayName("Should add multiple items to repository")
    void shouldAddMultipleItems() {
        repository.add(testItem1);
        repository.add(testItem2);

        assertEquals(2, repository.size());
        var items = repository.getAll();
        assertTrue(items.contains(testItem1));
        assertTrue(items.contains(testItem2));
    }

    @Test
    @DisplayName("Should retrieve all items")
    void shouldGetAllItems() {
        repository.add(testItem1);
        repository.add(testItem2);

        var items = repository.getAll();

        assertEquals(2, items.size());
    }

    @Test
    @DisplayName("Should return defensive copy of items")
    void shouldReturnDefensiveCopy() {
        repository.add(testItem1);

        var items1 = repository.getAll();
        var items2 = repository.getAll();

        assertNotSame(items1, items2);
        assertThrows(UnsupportedOperationException.class, items1::clear);
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Should find item by ID")
    void shouldFindItemById() {
        repository.add(testItem1);
        repository.add(testItem2);

        var found = repository.getById(testItem1.id());

        assertTrue(found.isPresent());
        assertEquals(testItem1, found.get());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    void shouldReturnEmptyForNonExistentId() {
        repository.add(testItem1);

        var found = repository.getById("non-existent-id");

        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should remove item by ID")
    void shouldRemoveItemById() {
        repository.add(testItem1);
        repository.add(testItem2);

        var removed = repository.remove(testItem1.id());

        assertTrue(removed);
        assertEquals(1, repository.size());
        assertTrue(repository.getById(testItem1.id()).isEmpty());
        assertTrue(repository.getById(testItem2.id()).isPresent());
    }

    @Test
    @DisplayName("Should return false when removing non-existent item")
    void shouldReturnFalseForNonExistentRemoval() {
        repository.add(testItem1);

        var removed = repository.remove("non-existent-id");

        assertFalse(removed);
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Should update existing item")
    void shouldUpdateItem() {
        repository.add(testItem1);

        testItem1.setTitle("Updated title");
        testItem1.setCompleted(true);
        repository.update(testItem1);

        var found = repository.getById(testItem1.id());
        assertTrue(found.isPresent());
        assertEquals("Updated title", found.get().title());
        assertTrue(found.get().completed());
    }

    @Test
    @DisplayName("Should handle update for non-existent item")
    void shouldHandleUpdateForNonExistentItem() {
        var nonExistentItem = new TodoItem("Non-existent", "Description");

        repository.update(nonExistentItem);

        assertEquals(0, repository.size());
    }

    @Test
    @DisplayName("Should clear all items")
    void shouldClearAllItems() {
        repository.add(testItem1);
        repository.add(testItem2);

        repository.clear();

        assertEquals(0, repository.size());
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    @DisplayName("Should maintain item order")
    void shouldMaintainItemOrder() {
        repository.add(testItem1);
        repository.add(testItem2);

        var items = repository.getAll();

        assertEquals(testItem1, items.get(0));
        assertEquals(testItem2, items.get(1));
    }

    @Test
    @DisplayName("Should handle adding same item multiple times")
    void shouldHandleAddingSameItemMultipleTimes() {
        repository.add(testItem1);
        repository.add(testItem1);

        assertEquals(2, repository.size());
    }

    @Test
    @DisplayName("Should update correct item when multiple items exist")
    void shouldUpdateCorrectItem() {
        repository.add(testItem1);
        repository.add(testItem2);

        testItem1.setTitle("Updated Task 1");
        repository.update(testItem1);

        var found1 = repository.getById(testItem1.id());
        var found2 = repository.getById(testItem2.id());

        assertTrue(found1.isPresent());
        assertEquals("Updated Task 1", found1.get().title());

        assertTrue(found2.isPresent());
        assertEquals("Task 2", found2.get().title());
    }
}
