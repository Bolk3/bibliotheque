<h1 align="center">System de Gestion de Bibliotheque</h1>

<p align="center">
    <img src="https://img.shields.io/badge/progression-50%25-green?style=for-the-badge"/>
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
    <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white"/>
</p>

---

## Features
### Gestion des Ressources
- **Multimedia :** Comprehensive handling of Books and DVDs.
- **Events :** Conference organization and speaker management.

### Administration & Utilisateurs
- **Security :** Librarian management with hierarchical clearance levels.
- **Members :** Loan tracking and automated penalty system.

### Qualité du Code
- **Unit Testing :** Full system coverage using **JUnit 5**.

## Build
### Prerequisites
* Java JDK 11+
* JUnit 5 (for testing)
* Make (optional)

### Make commands
| Command       | Use                       |
| ------------- | ------------------------- |
| `make`        | Compile the project       |
| `make test`   | Make the JUnit tests      |
| `make run`    | Launch the app            |
| `make clean`  | Delete `out/`             |
| `make fclean` | Delete `out/` and `lib/`  |
| `make re`     | fclean + make             |

### Build, launch and test without make
If you don't have `make` installed:

**Compilation:**
```bash
mkdir -p out
javac -d out -sourcepath src src/com/bibliotheque/Main.java
```

**Running:**
```bash
java -cp out com.bibliotheque.Main
```

**Testing:**
```bash
mkdir -p out lib
curl -L -o lib/junit-platform-console-standalone-1.10.2.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
javac -d out -cp "lib/junit-platform-console-standalone-1.10.2.jar" -encoding UTF-8 $(find src test -name "*.java")
java -jar lib/junit-platform-console-standalone-1.10.2.jar --class-path out --scan-class-path
```

## UML

![uml](./images/Screenshot_20260402_091052.png)