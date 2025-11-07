# Mixing Kotlin and Java in a Maven project

This guide shows how to configure a Maven project to use both Kotlin and Java together.

## Step 1: Add Kotlin version property

Add the Kotlin version property to your `pom.xml`:

```xml
<properties>
    <!-- ... other properties ... -->
    <kotlin.version>2.2.21</kotlin.version>
</properties>
```

## Step 2: Add dependencies

Add the required dependencies:

```xml
<dependencies>
    <!-- ... existing dependencies ... -->

    <!-- JUnit Jupiter engine is required at test runtime -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <scope>test</scope>
    </dependency>

    <!-- Kotlin standard library needed to compile/run Kotlin tests -->
    <dependency>
        <groupId>org.jetbrains.kotlin</groupId>
        <artifactId>kotlin-stdlib</artifactId>
        <version>${kotlin.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Step 3: Configure Kotlin Maven plugin

You only need the Kotlin plugin under `<build><plugins>`. Bind it to the Java compile phases and point it at both Java and Kotlin source roots so code can reference each other both ways.

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
            <executions>
                <execution>
                    <id>default-compile</id>
                    <phase>compile</phase>
                    <configuration>
                        <sourceDirs>
                            <sourceDir>src/main/kotlin</sourceDir>
                            <!-- Ensure Kotlin code can reference Java code -->
                            <sourceDir>src/main/java</sourceDir>
                        </sourceDirs>
                    </configuration>
                </execution>
                <execution>
                    <id>default-test-compile</id>
                    <phase>test-compile</phase>
                    <configuration>
                        <sourceDirs>
                            <sourceDir>src/test/kotlin</sourceDir>
                            <sourceDir>src/test/java</sourceDir>
                        </sourceDirs>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

**Why this configuration?**

- Keeping `<extensions>true</extensions>` lets Maven wire the Kotlin plugin into the lifecycle seamlessly.
- This allows Kotlin code to reference Java code and vice versa
- The custom execution phases allow the Kotlin plugin to successfully compile Kotlin and then Java.

## Step 4: Verify configuration

Run your tests to verify the setup:

```bash
./mvnw clean test
```

## Directory structure

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

The `kotlin-maven-plugin` configuration includes both `src/main/java` and `src/test/java` directories, so you can place `.kt` files alongside `.java` files in the same directories.

## Summary

- **Property**: Added `kotlin.version` property
- **Dependency**: Added `kotlin-stdlib` with test scope and `junit-jupiter-engine` that is required for test runtime
- **Plugin**: Configured `kotlin-maven-plugin` under `<build><plugins>` section

The configuration ensures that:

- Kotlin compiler runs before Java compiler
- Both Java and Kotlin code can reference each other
