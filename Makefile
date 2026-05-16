SRC_DIR    = src
OUT_DIR    = out
TEST_DIR   = test
TEST_OUT   = out/test
LIB_DIR    = lib
MAIN_CLASS = com.bibliotheque.Main

JUNIT_JAR  = $(LIB_DIR)/junit-platform-console-standalone.jar
JUNIT_URL  = https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar

SOURCES    = $(shell find $(SRC_DIR) -name "*.java" 2>/dev/null)
TEST_SRC   = $(shell find $(TEST_DIR) -name "*.java" 2>/dev/null)

JAVAC      = javac
JFLAGS     = -d $(OUT_DIR) -cp "$(OUT_DIR):$(LIB_DIR)/*" -encoding UTF-8

.PHONY: all setup compile run clean fclean re test help

all: compile

setup:
	@mkdir -p $(LIB_DIR) $(OUT_DIR) $(TEST_DIR) $(TEST_OUT)
	@if [ ! -f "$(JUNIT_JAR)" ]; then \
		curl -L -o $(JUNIT_JAR) $(JUNIT_URL); \
	fi

compile: setup
	@if [ -z "$(SOURCES)" ]; then echo "Erreur: Aucune source dans $(SRC_DIR)"; exit 1; fi
	@$(JAVAC) $(JFLAGS) $(SOURCES)
	@if [ -d "$(SRC_DIR)/assets" ]; then cp -r $(SRC_DIR)/assets $(OUT_DIR)/; fi

run: compile
	@java -cp "$(OUT_DIR):$(LIB_DIR)/*" $(MAIN_CLASS)

clean:
	@rm -rf $(OUT_DIR)

fclean: clean
	@rm -rf $(LIB_DIR)

re: fclean all

test: compile
	@if [ -z "$(TEST_SRC)" ]; then echo "Erreur: Aucun test dans $(TEST_DIR)"; exit 1; fi
	@$(JAVAC) -d $(TEST_OUT) -cp "$(OUT_DIR):$(JUNIT_JAR)" -encoding UTF-8 $(TEST_SRC)
	@java -jar $(JUNIT_JAR) --class-path "$(OUT_DIR):$(TEST_OUT)" --scan-class-path

# ─────────────────────────────────────────────
#  Aide
# ─────────────────────────────────────────────
help:
	@echo "Available commands :"
	@echo "  make        : Compile the project"
	@echo "  make run    : Launch $(MAIN_CLASS)"
	@echo "  make clean  : Delete $(OUT_DIR)/"
	@echo "  make fclean : Delete $(OUT_DIR)/ and $(LIB_DIR)/"
	@echo "  make re     : fclean + make"
	@echo "  make test   : Compile and run the tests"