package token;

// Enums are the standard way to represent a fixed set of constants,
// which is what token types are.
public enum TokenType {
    // You would list all your possible token types here
    ILLEGAL,
    EOF,
    
    // Identifiers + Literals
    IDENT, // add, foobar, x, y, ...
    INT,   // 12345
    
    // Operators
    ASSIGN,
    PLUS,
    
    // Delimiters
    COMMA,
    SEMICOLON,
    
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    
    // Keywords
    FUNCTION,
    LET
    // ... etc.
}
