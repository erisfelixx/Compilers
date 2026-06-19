import java.util.Set;

public class Scanner {
    private final String input;
    private int pos = 0;
    private int line = 1;
    private int column = 1;

    //купка ключових слів
    private static final Set<String> KEYWORDS = Set.of(
            "val", "var", "def", "class", "object", "trait", 
            "if", "else", "true", "false", "return"
    );

    public Scanner(String input) {
        this.input = input;
    }

    public Token nextToken() {
        //пропускаємо лише "безпечні" пробіли, залишаючи \n для аналізу
        //(у Scala перенесення рядка \n часто працює як невидима крапка з комою)
        skipWhitespace();

        if (pos >= input.length()) {
            return new Token(TokenType.EOF, "", line, column);
        }

        char currentChar = peek();
        int startCol = column;

        //обробка перенесення рядка
        if (currentChar == '\n') {
            int currentLine = line;
            pos++;        //пересуваємо курсор вперед вручну
            line++;       //новий рядок
            column = 1;   //наступний символ буде на 1-й позиції нового рядка
            //повертаємо \n як обмежувач (DELIMITER)
            return new Token(TokenType.DELIMITER, "\\n", currentLine, startCol);
        }

        // 1) однорядкові string-літерали
        if (currentChar == '"') {
            return parseStringLiteral();
        }

        // 2) ідентифікатори та ключові слова
        if (Character.isLetter(currentChar) || currentChar == '_') {
            return parseIdentifierOrKeyword();
        }

        // 3) префіксні оператори
        if (currentChar == '+' || currentChar == '-' || currentChar == '!' || currentChar == '~') {
            advance();
            return new Token(TokenType.PREFIX_OP, String.valueOf(currentChar), line, startCol);
        }

        // 4) присвоєння
        if (currentChar == '=') {
            advance();
            return new Token(TokenType.ASSIGN, "=", line, startCol);
        }

        // 5) дужки та інші обмежувачі (, ;)
        switch (currentChar) {
            case '(': advance(); return new Token(TokenType.LPAREN, "(", line, startCol);
            case ')': advance(); return new Token(TokenType.RPAREN, ")", line, startCol);
            case '{': advance(); return new Token(TokenType.LBRACE, "{", line, startCol);
            case '}': advance(); return new Token(TokenType.RBRACE, "}", line, startCol);
            case '[': advance(); return new Token(TokenType.LBRACKET, "[", line, startCol);
            case ']': advance(); return new Token(TokenType.RBRACKET, "]", line, startCol);
            case ',': 
            case ';': 
                advance(); return new Token(TokenType.DELIMITER, String.valueOf(currentChar), line, startCol);
        }

        // !якщо символ не розпізнано
        advance();
        return new Token(TokenType.ERROR, String.valueOf(currentChar), line, startCol);
    }

    //-- дпоміжні методи --

    private Token parseStringLiteral() {
        int startCol = column;
        StringBuilder sb = new StringBuilder();
        
        advance(); //пропускаємо початкову лапку
        
        while (pos < input.length() && peek() != '"') {
            // якщо зустріли перенесення рядка всередині літералу - це помилка
            if (peek() == '\n') {
                return new Token(TokenType.ERROR, "Unterminated string literal", line, startCol);
            }
            sb.append(peek());
            advance();
        }
        
        if (pos >= input.length()) {
            return new Token(TokenType.ERROR, "Unterminated string literal at EOF", line, startCol);
        }
        
        advance(); // пропускаємо кінцеву лапку
        return new Token(TokenType.STRING_LITERAL, sb.toString(), line, startCol);
    }

    private Token parseIdentifierOrKeyword() {
        int startCol = column;
        StringBuilder sb = new StringBuilder();

        while (pos < input.length() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(peek());
            advance();
        }

        String value = sb.toString();
        TokenType type = KEYWORDS.contains(value) ? TokenType.KEYWORD : TokenType.IDENTIFIER;
        
        return new Token(type, value, line, startCol);
    }

    private void skipWhitespace() {
        while (pos < input.length()) {
            char c = peek();
            //ігноруємо пробіл, табуляцію та повернення каретки
            //символ \n ми ТУТ НЕ ігноруємо
            if (c == ' ' || c == '\t' || c == '\r') {
                advance();
            } else {
                break;
            }
        }
    }

    private char peek() {
        return input.charAt(pos);
    }

    private void advance() {
        pos++;
        column++;
    }
}