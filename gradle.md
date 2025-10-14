# Mixing Kotlin and Java in a Gradle Project

This guide shows how to configure a Gradle project to use both Kotlin and Java together.

## Step 1: Add Kotlin JVM Plugin

Add the Kotlin JVM plugin to your `build.gradle.kts`:

```kotlin
plugins {
    // ... existing plugins ...
    kotlin("jvm") version "2.2.20"
}
```

## Step 2: Configure Kotlin JVM Toolchain

Set the JVM toolchain version to match your Java version:

```kotlin
kotlin {
    jvmToolchain(17)
}
```

This ensures Kotlin uses the same JDK version as your Java code.

## Step 3: Add Kotlin Test Dependency

Add the Kotlin test library to your dependencies:

```kotlin
dependencies {
    // ... existing dependencies ...

    testImplementation(kotlin("test"))
    // ... other test dependencies ...
}
```

The `kotlin("test")` dependency provides Kotlin's test utilities and integrates with JUnit.

## Step 4: Verify Configuration

Run your tests to verify the configuration:

```bash
./gradlew clean test
```

## Directory Structure

With this configuration, you can mix Java and Kotlin files in the same source directories:

```
src/
├── main/
│   ├── java/          # Java and Kotlin production code
│   └── kotlin/        # Additional Kotlin production code (optional)
└── test/
    ├── java/          # Java and Kotlin test code
    └── kotlin/        # Additional Kotlin test code (optional)
```

The Kotlin plugin automatically recognizes both `src/main/java` and `src/test/java` directories, so you can place `.kt` files alongside `.java` files in the same directories.

## Summary of Changes

1. **Plugin**: Added `kotlin("jvm")` plugin with version 2.2.20
2. **Toolchain**: Configured `jvmToolchain(17)` to match Java version
3. **Dependency**: Added `kotlin("test")` test dependency

The configuration ensures that:
- Kotlin compiler runs before Java compiler
- Both Java and Kotlin code can reference each other
