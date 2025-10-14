# Kotlin JUnit Sample

Tutorial: [Test code using JUnit in JVM](https://kotlinlang.org/docs/jvm-test-using-junit.htm)

Configuration guides: [Maven](maven.md) | [Gradle](gradle.md)

A sample project demonstrating how to write tests in Kotlin for Java applications. This project shows the migration path from Java tests to Kotlin tests while testing Java source code.

## Getting Started

![Todo Application Screenshot](misc/img.png)

### Prerequisites

- Java 17 or higher

### Running Tests

Using Maven:
```bash
cd initial  # or cd complete
./mvnw test
```

Using Gradle:
```bash
cd initial  # or cd complete
./gradlew test
```

## Project Structure

This repository contains two versions of the same project:

```
kotlin-junit-sample/
├── initial/          # Starting point with Java tests
│   ├── src/
│   │   ├── main/java/    # Java source code
│   │   └── test/java/    # JUnit tests in Java
│   ├── pom.xml           # Maven configuration
│   └── build.gradle.kts  # Gradle configuration
│
└── complete/         # Final version with Kotlin tests
    ├── src/
    │   ├── main/java/    # Same Java source code
    │   └── test/java/    # JUnit tests in Kotlin
    ├── pom.xml           # Maven with Kotlin support
    └── build.gradle.kts  # Gradle with Kotlin plugin
```

### Application Code

Both projects contain a simple Todo application with:
- **TodoItem** - A Java class representing a todo item with title, description, completion status, and timestamps
- **TodoRepository** - A Java repository class for managing todo items in memory

### Test Code

- **initial/**: Contains comprehensive JUnit 5 tests written in Java
- **complete/**: Same tests converted to Kotlin, demonstrating idiomatic Kotlin testing patterns

## Technologies

- **Java**: 17
- **Kotlin**: 2.2.20 (test code only in complete version)
- **JUnit**: 5.11.0
- **Build Tools**: Maven and Gradle (both supported)
- **UI Library**: Jexer 1.6.0 (terminal UI library)
