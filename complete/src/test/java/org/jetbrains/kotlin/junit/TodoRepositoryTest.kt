package org.jetbrains.kotlin.junit

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class TodoRepositoryTest {
    lateinit var repository: TodoRepository
    lateinit var testItem1: TodoItem
    lateinit var testItem2: TodoItem

    @BeforeEach
    fun setUp() {
        repository = TodoRepository()
        testItem1 = TodoItem("Task 1", "Description 1")
        testItem2 = TodoItem("Task 2", "Description 2")
    }

    @Test
    @DisplayName("Should start with empty repository")
    fun shouldStartEmpty() {
        Assertions.assertEquals(0, repository.size())
        Assertions.assertTrue(repository.all.isEmpty())
    }

    @Test
    @DisplayName("Should add item to repository")
    fun shouldAddItem() {
        repository.add(testItem1)

        Assertions.assertEquals(1, repository.size())
        Assertions.assertTrue(repository.all.contains(testItem1))
    }

    @Test
    @DisplayName("Should add multiple items to repository")
    fun shouldAddMultipleItems() {
        repository.add(testItem1)
        repository.add(testItem2)

        Assertions.assertEquals(2, repository.size())
        val items = repository.all
        Assertions.assertTrue(items.contains(testItem1))
        Assertions.assertTrue(items.contains(testItem2))
    }

    @Test
    @DisplayName("Should retrieve all items")
    fun shouldGetAllItems() {
        repository.add(testItem1)
        repository.add(testItem2)

        val items = repository.all

        Assertions.assertEquals(2, items.size)
    }

    @Test
    @DisplayName("Should return defensive copy of items")
    fun shouldReturnDefensiveCopy() {
        repository.add(testItem1)

        val items1 = repository.all
        val items2 = repository.all

        Assertions.assertNotSame(items1, items2)
        Assertions.assertThrows(
            UnsupportedOperationException::class.java
        ) { items1.clear() }
        Assertions.assertEquals(1, repository.size())
    }

    @Test
    @DisplayName("Should find item by ID")
    fun shouldFindItemById() {
        repository.add(testItem1)
        repository.add(testItem2)

        val found = repository.getById(testItem1.id())

        Assertions.assertTrue(found.isPresent)
        Assertions.assertEquals(testItem1, found.get())
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    fun shouldReturnEmptyForNonExistentId() {
        repository.add(testItem1)

        val found = repository.getById("non-existent-id")

        Assertions.assertTrue(found.isEmpty)
    }

    @Test
    @DisplayName("Should remove item by ID")
    fun shouldRemoveItemById() {
        repository.add(testItem1)
        repository.add(testItem2)

        val removed = repository.remove(testItem1.id())

        Assertions.assertTrue(removed)
        Assertions.assertEquals(1, repository.size())
        Assertions.assertTrue(repository.getById(testItem1.id()).isEmpty)
        Assertions.assertTrue(repository.getById(testItem2.id()).isPresent)
    }

    @Test
    @DisplayName("Should return false when removing non-existent item")
    fun shouldReturnFalseForNonExistentRemoval() {
        repository.add(testItem1)

        val removed = repository.remove("non-existent-id")

        Assertions.assertFalse(removed)
        Assertions.assertEquals(1, repository.size())
    }

    @Test
    @DisplayName("Should update existing item")
    fun shouldUpdateItem() {
        repository.add(testItem1)

        testItem1.setTitle("Updated title")
        testItem1.setCompleted(true)
        repository.update(testItem1)

        val found = repository.getById(testItem1.id())
        Assertions.assertTrue(found.isPresent)
        Assertions.assertEquals("Updated title", found.get().title())
        Assertions.assertTrue(found.get().completed())
    }

    @Test
    @DisplayName("Should handle update for non-existent item")
    fun shouldHandleUpdateForNonExistentItem() {
        val nonExistentItem = TodoItem("Non-existent", "Description")

        repository.update(nonExistentItem)

        Assertions.assertEquals(0, repository.size())
    }

    @Test
    @DisplayName("Should clear all items")
    fun shouldClearAllItems() {
        repository.add(testItem1)
        repository.add(testItem2)

        repository.clear()

        Assertions.assertEquals(0, repository.size())
        Assertions.assertTrue(repository.all.isEmpty())
    }

    @Test
    @DisplayName("Should maintain item order")
    fun shouldMaintainItemOrder() {
        repository.add(testItem1)
        repository.add(testItem2)

        val items = repository.all

        Assertions.assertEquals(testItem1, items[0])
        Assertions.assertEquals(testItem2, items[1])
    }

    @Test
    @DisplayName("Should handle adding same item multiple times")
    fun shouldHandleAddingSameItemMultipleTimes() {
        repository.add(testItem1)
        repository.add(testItem1)

        Assertions.assertEquals(2, repository.size())
    }

    @Test
    @DisplayName("Should update correct item when multiple items exist")
    fun shouldUpdateCorrectItem() {
        repository.add(testItem1)
        repository.add(testItem2)

        testItem1.setTitle("Updated Task 1")
        repository.update(testItem1)

        val found1 = repository.getById(testItem1.id())
        val found2 = repository.getById(testItem2.id())

        Assertions.assertTrue(found1.isPresent)
        Assertions.assertEquals("Updated Task 1", found1.get().title())

        Assertions.assertTrue(found2.isPresent)
        Assertions.assertEquals("Task 2", found2.get().title())
    }
}
