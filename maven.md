# Mixing Kotlin and Java in a Maven project

This guide shows how to configure a Maven project to use both Kotlin and Java together.

## Step 1: Add Kotlin version property

Add the Kotlin version property to your `pom.xml`:

```xml
<properties>
    <!-- ... other properties ... -->
    <kotlin.version>2.3.20</kotlin.version>
</properties>
```

## Step 2: Add dependencies

Add the JUnit dependency:

```xml
<dependencies>
    <!-- ... existing dependencies ... -->

    <!-- JUnit Jupiter engine is required at test runtime -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Step 3: Configure Kotlin Maven plugin

You only need the Kotlin plugin configuration under `<build><plugins>`:

```xml
<build>
    <pluginManagement>
        <!-- No maven-compiler-plugin needed with Kotlin extensions -->
    </pluginManagement>

    <plugins>
        <!-- Activate Kotlin for main and test sources -->
        <plugin>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-plugin</artifactId>
            <version>${kotlin.version}</version>
            <extensions>true</extensions>
        </plugin>
    </plugins>
</build>
```

**Why this configuration?**

Enabling `<extensions>true</extensions>` in the Kotlin Maven plugin helps us to:

* Register `src/main/kotlin` and `src/test/kotlin` directories as source roots.
* Add the `kotlin-stdlib` dependency to the project.
* Reference Kotlin code in Java code and vice versa.
* Successfully compile Kotlin and then Java together.

## Step 4: Verify configuration

Run your tests to verify the setup:

```bash
./mvnw clean test
```

## Directory structure

With this configuration, you can mix Java and Kotlin files in the same source directories:

```none
src/
├── main/
│   ├── java/          # Java and Kotlin production code
│   └── kotlin/        # Additional Kotlin production code (optional)
└── test/
    ├── java/          # Java and Kotlin test code
    └── kotlin/        # Additional Kotlin test code (optional)
```

The `kotlin-maven-plugin` configuration registers both `src/main/java` and `src/test/java` directories, so you can place `.kt` files alongside `.java` files in the same directories.

## Summary

- **Property**: Added `kotlin.version` property.
- **Dependency**: Added `junit-jupiter-engine` that is required for test runtime.
- **Plugin**: Configured `kotlin-maven-plugin` under `<build><plugins>` section.

The configuration ensures that:

- Kotlin compiler runs before Java compiler
- Both Java and Kotlin code can reference each other
