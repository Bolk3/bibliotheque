SRC_DIR   = src
OUT_DIR   = out

MAIN_CLASS = com.bibliotheque.Main

SOURCES   = $(shell find $(SRC_DIR) -name "*.java" 2>/dev/null)

JAVAC     = javac
JFLAGS    = -d $(OUT_DIR) -cp "$(OUT_DIR):$(LIB_DIR)/*" -encoding UTF-8

.PHONY: all setup compile run clean fclean re help

all: compile

setup:
	@mkdir -p $(LIB_DIR) $(OUT_DIR)

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

# ─────────────────────────────────────────────
#  Aide
# ─────────────────────────────────────────────
help:
	@echo "Commandes disponibles :"
	@echo "  make         : Compile le projet"
	@echo "  make run     : Lance la classe $(MAIN_CLASS)"
	@echo "  make clean   : Supprime le dossier $(OUT_DIR)"
	@echo "  make fclean  : Supprime $(OUT_DIR) et $(LIB_DIR)"
	@echo "  make re      : fclean + make"