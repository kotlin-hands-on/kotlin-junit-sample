# Mixing Kotlin and Java in a Maven Project

This guide shows how to configure a Maven project to use both Kotlin and Java together.

## Step 1: Add Kotlin Version Property

Add the Kotlin version property to your `pom.xml`:

```xml
<properties>
    <!-- ... other properties ... -->
    <kotlin.version>2.2.20</kotlin.version>
</properties>
```

## Step 2: Add Dependencies

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

## Step 3: Configure Kotlin Maven Plugin

Add the Kotlin Maven plugin configuration to `<pluginManagement>`:

```xml
<build>
    <pluginManagement>
        <plugins>
            <!-- ... other plugins ... -->

            <plugin>
                <groupId>org.jetbrains.kotlin</groupId>
                <artifactId>kotlin-maven-plugin</artifactId>
                <version>${kotlin.version}</version>
                <extensions>true</extensions>
                <executions>
                    <execution>
                        <id>compile</id>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                        <configuration>
                            <sourceDirs>
                                <sourceDir>${project.basedir}/src/main/kotlin</sourceDir>
                                <sourceDir>${project.basedir}/src/main/java</sourceDir>
                            </sourceDirs>
                        </configuration>
                    </execution>
                    <execution>
                        <id>test-compile</id>
                        <goals>
                            <goal>test-compile</goal>
                        </goals>
                        <configuration>
                            <sourceDirs>
                                <sourceDir>${project.basedir}/src/test/kotlin</sourceDir>
                                <sourceDir>${project.basedir}/src/test/java</sourceDir>
                            </sourceDirs>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```

## Step 4: Configure Maven Compiler Plugin for Mixed Compilation

Update the Maven Compiler plugin to work with Kotlin. Add this to `<pluginManagement>`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.14.0</version>
    <executions>
        <!-- Disable default compile phases -->
        <execution>
            <id>default-compile</id>
            <phase>none</phase>
        </execution>
        <execution>
            <id>default-testCompile</id>
            <phase>none</phase>
        </execution>
        <!-- Enable Java compilation after Kotlin -->
        <execution>
            <id>java-compile</id>
            <phase>compile</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
        <execution>
            <id>java-test-compile</id>
            <phase>test-compile</phase>
            <goals>
                <goal>testCompile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Why this configuration?**
- Disabling default Maven compiler phases ensures Kotlin compiles first
- This allows Kotlin code to reference Java code and vice versa
- The custom execution phases run Java compilation after Kotlin

## Step 5: Activate the Kotlin Plugin

Add the Kotlin plugin to the `<plugins>` section (outside `<pluginManagement>`):

```xml
<build>
    <pluginManagement>
        <!-- ... -->
    </pluginManagement>

    <plugins>
        <plugin>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-plugin</artifactId>
            <version>${kotlin.version}</version>
            <extensions>true</extensions>
            <executions>
                <execution>
                    <id>compile</id>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                    <configuration>
                        <sourceDirs>
                            <sourceDir>${project.basedir}/src/main/kotlin</sourceDir>
                            <sourceDir>${project.basedir}/src/main/java</sourceDir>
                        </sourceDirs>
                    </configuration>
                </execution>
                <execution>
                    <id>test-compile</id>
                    <goals>
                        <goal>test-compile</goal>
                    </goals>
                    <configuration>
                        <sourceDirs>
                            <sourceDir>${project.basedir}/src/test/kotlin</sourceDir>
                            <sourceDir>${project.basedir}/src/test/java</sourceDir>
                        </sourceDirs>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Step 6: Verify Configuration

Run your tests to verify the configuration:

```bash
./mvnw clean test
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

The `kotlin-maven-plugin` configuration includes both `src/main/java` and `src/test/java` directories, so you can place `.kt` files alongside `.java` files in the same directories.

## Summary of Changes

1. **Property**: Added `kotlin.version` property
2. **Dependency**: Added `kotlin-stdlib` with test scope
3. **Dependency**: Added `junit-jupiter-engine` (required for test runtime)
4. **Plugin**: Added `kotlin-maven-plugin` configuration
5. **Plugin**: Modified `maven-compiler-plugin` to disable default phases
6. **Plugin**: Activated `kotlin-maven-plugin` in `<plugins>` section

The configuration ensures that:
- Kotlin compiler runs before Java compiler
- Both Java and Kotlin code can reference each other
