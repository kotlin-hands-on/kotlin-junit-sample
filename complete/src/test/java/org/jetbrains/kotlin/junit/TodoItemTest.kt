package org.jetbrains.kotlin.junit

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime

internal class TodoItemTest {
    @Test
    @DisplayName("Should create a new todo item with title and description")
    fun shouldCreateTodoItem() {
        val item = TodoItem("Buy groceries", "Milk, eggs, bread")

        Assertions.assertNotNull(item.id())
        Assertions.assertEquals("Buy groceries", item.title())
        Assertions.assertEquals("Milk, eggs, bread", item.description())
        Assertions.assertFalse(item.completed())
        Assertions.assertNotNull(item.createdAt())
        Assertions.assertNotNull(item.updatedAt())
    }

    @Test
    @DisplayName("Should generate unique IDs for different items")
    fun shouldGenerateUniqueIds() {
        val item1 = TodoItem("Task 1", "Description 1")
        val item2 = TodoItem("Task 2", "Description 2")

        Assertions.assertNotEquals(item1.id(), item2.id())
    }

    @Test
    @DisplayName("Should update title and updatedAt timestamp")
    fun shouldUpdateTitle() {
        val item = TodoItem("Old title", "Description")
        val originalUpdatedAt = item.updatedAt()

        try {
            Thread.sleep(10)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        item.setTitle("New title")

        Assertions.assertEquals("New title", item.title())
        Assertions.assertTrue(item.updatedAt().isAfter(originalUpdatedAt))
    }

    @Test
    @DisplayName("Should update description and updatedAt timestamp")
    fun shouldUpdateDescription() {
        val item = TodoItem("Title", "Old description")
        val originalUpdatedAt = item.updatedAt()

        try {
            Thread.sleep(10)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        item.setDescription("New description")

        Assertions.assertEquals("New description", item.description())
        Assertions.assertTrue(item.updatedAt().isAfter(originalUpdatedAt))
    }

    @Test
    @DisplayName("Should toggle completion status")
    fun shouldToggleCompletion() {
        val item = TodoItem("Task", "Description")

        Assertions.assertFalse(item.completed())

        item.setCompleted(true)
        Assertions.assertTrue(item.completed())

        item.setCompleted(false)
        Assertions.assertFalse(item.completed())
    }

    @Test
    @DisplayName("Should update updatedAt when setting completion status")
    fun shouldUpdateTimestampOnCompletion() {
        val item = TodoItem("Task", "Description")
        val originalUpdatedAt = item.updatedAt()

        try {
            Thread.sleep(10)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        item.setCompleted(true)

        Assertions.assertTrue(item.updatedAt().isAfter(originalUpdatedAt))
    }

    @Test
    @DisplayName("Should format toString correctly for incomplete task")
    fun shouldFormatToStringForIncompleteTask() {
        val item = TodoItem("Buy milk", "From store")

        val result = item.toString()

        Assertions.assertTrue(result.contains("[ ]"))
        Assertions.assertTrue(result.contains("Buy milk"))
        Assertions.assertTrue(result.contains("From store"))
    }

    @Test
    @DisplayName("Should format toString correctly for completed task")
    fun shouldFormatToStringForCompletedTask() {
        val item = TodoItem("Buy milk", "From store")
        item.setCompleted(true)

        val result = item.toString()

        Assertions.assertTrue(result.contains("[X]"))
        Assertions.assertTrue(result.contains("Buy milk"))
        Assertions.assertTrue(result.contains("From store"))
    }

    @Test
    @DisplayName("Should compare items by ID for equality")
    fun shouldCompareById() {
        val item1 = TodoItem("Task", "Description")
        val item2 = TodoItem(
            "id", "Task", "Description", false,
            LocalDateTime.now(), LocalDateTime.now()
        )

        Assertions.assertNotEquals(item1, item2)

        val item3 = TodoItem(
            item1.id(), "Different", "Different", true,
            LocalDateTime.now(), LocalDateTime.now()
        )

        Assertions.assertEquals(item1, item3)
        Assertions.assertEquals(item1.hashCode(), item3.hashCode())
    }

    @Test
    @DisplayName("Should not be equal to null or different class")
    fun shouldHandleNullAndDifferentClass() {
        val item = TodoItem("Task", "Description")

        Assertions.assertNotEquals(null, item)
        Assertions.assertNotEquals("String", item)
    }

    @Test
    @DisplayName("Should maintain createdAt timestamp")
    fun shouldMaintainCreatedAt() {
        val item = TodoItem("Task", "Description")
        val createdAt = item.createdAt()

        item.setTitle("New title")
        item.setDescription("New description")
        item.setCompleted(true)

        Assertions.assertEquals(createdAt, item.createdAt())
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   ", "A", "This is a very long title for a todo item"])
    @DisplayName("Should handle various title lengths")
    fun shouldHandleVariousTitles(title: String?) {
        val item = TodoItem(title, "Description")

        Assertions.assertEquals(title, item.title())
    }
}
