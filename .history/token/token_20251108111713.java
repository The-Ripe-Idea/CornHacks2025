package token;
import


public class Token {
    private final TokenType type;
    private final String literal;

    public Token(TokenType type, String literal) {
        this.type = type;
        this.literal = literal;
    }

    public TokenType getType() {
        return type;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + this.type +
                ", literal='" + literal + '\'' +
                '}';
    }
    
    // You would also typically override equals() and hashCode()
}
