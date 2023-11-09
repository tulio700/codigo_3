import java.io.IOException;

import Reader;

public class LexicalAnalyzer {

    private Reader reader;
    private int currentChar;

    public LexicalAnalyzer(Reader reader) {
        this.reader = reader;
        try {
            currentChar = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Token nextToken() throws IOException {
        while (isWhitespace(currentChar)) {
            advance();
        }

        if (currentChar == -1) {
            return new Token(TokenType.EOF, null);
        }

        if (isLetter(currentChar)) {
            return handleIdentifier();
        }

        if (isDigit(currentChar)) {
            return handleNumber();
        }

        switch (currentChar) {
            case '+':
                advance();
                return new Token(TokenType.PLUS, "+");
            case '-':
                advance();
                return new Token(TokenType.MINUS, "-");
            case '*':
                advance();
                return new Token(TokenType.STAR, "*");
            case '/':
                advance();
                return new Token(TokenType.SLASH, "/");
            case '=':
                advance();
                return new Token(TokenType.ASSIGN, "=");
            case '(':
                advance();
                return new Token(TokenType.LPAREN, "(");
            case ')':
                advance();
                return new Token(TokenType.RPAREN, ")");
            case ';':
                advance();
                return new Token(TokenType.SEMICOLON, ";");
            default:
                throw new IllegalStateException("Invalid character: " + (char) currentChar);
        }
    }

    private Token handleIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (isLetterOrDigit(currentChar)) {
            builder.append((char) currentChar);
            advance();
        }

        String lexeme = builder.toString();
        TokenType type = TokenType.IDENTIFIER;
        if (lexeme.equals("int")) {
            type = TokenType.INT_KEYWORD;
        } else if (lexeme.equals("real")) {
            type = TokenType.REAL_KEYWORD;
        }

        return new Token(type, lexeme);
    }

    private Token handleNumber() {
        StringBuilder builder = new StringBuilder();
        while (isDigit(currentChar)) {
            builder.append((char) currentChar);
            advance();
        }

        if (currentChar == '.') {
            builder.append('.');
            advance();

            while (isDigit(currentChar)) {
                builder.append((char) currentChar);
                advance();
            }

            return new Token(TokenType.REAL_NUMBER, builder.toString());
        } else {
            return new Token(TokenType.INT_NUMBER, builder.toString());
        }
    }

    private void advance() throws IOException {
        currentChar = reader.read();
    }

    private boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean isLetter(int c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }

    private boolean isLetterOrDigit(int c) {
        return isLetter(c) || isDigit(c);
    }
}
