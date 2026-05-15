SRC_DIR   = src
TEST_DIR  = test
OUT_DIR   = out
LIB_DIR   = lib

MAIN_CLASS = com.bibliotheque.Main

JUNIT_JAR = $(LIB_DIR)/junit-platform-console-standalone-1.10.2.jar
JUNIT_URL = https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar

SOURCES   = $(shell find $(SRC_DIR) -name "*.java" 2>/dev/null)
TESTS     = $(shell find $(TEST_DIR) -name "*.java" 2>/dev/null)

JAVAC     = javac
JFLAGS    = -d $(OUT_DIR) -cp "$(OUT_DIR):$(JUNIT_JAR)" -encoding UTF-8

.PHONY: all setup compile test run clean fclean re help

all: compile

setup:
	@mkdir -p $(LIB_DIR) $(OUT_DIR)
	@if [ ! -f $(JUNIT_JAR) ]; then \
		curl -L -o $(JUNIT_JAR) $(JUNIT_URL); \
	fi

compile: setup
	@if [ -z "$(SOURCES)" ]; then echo "Erreur: Aucune source dans $(SRC_DIR)"; exit 1; fi
	@$(JAVAC) $(JFLAGS) $(SOURCES)
	@cp -r $(SRC_DIR)/assets $(OUT_DIR)/

test: compile
	@if [ -z "$(TESTS)" ]; then echo "Erreur: Aucun test dans $(TEST_DIR)"; exit 1; fi
	@$(JAVAC) $(JFLAGS) $(TESTS)
	@java -jar $(JUNIT_JAR) --class-path $(OUT_DIR) --scan-class-path

run: compile
	@java -cp "$(OUT_DIR):$(JUNIT_JAR)" $(MAIN_CLASS)

clean:
	@rm -rf $(OUT_DIR)

fclean: clean
	@rm -rf $(LIB_DIR)

re: fclean all

# ─────────────────────────────────────────────
#  Aide
# ─────────────────────────────────────────────
help:
	@echo "Commandes disponibles :"
	@echo "  make         : Compile le projet"
	@echo "  make test    : Lance les tests JUnit"
	@echo "  make run     : Lance la classe $(MAIN_CLASS)"
	@echo "  make clean   : Supprime le dossier $(OUT_DIR)"
	@echo "  make fclean  : Supprime $(OUT_DIR) et $(LIB_DIR)"
	@echo "  make re      : fclean + make"