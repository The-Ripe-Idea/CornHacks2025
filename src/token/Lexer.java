package token;

public class Lexer {
    public record Token(TokenType type, String literal) {}
}
