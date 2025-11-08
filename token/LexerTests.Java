import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class LexerTests {
    @Test
    public void testNextToken() {
        String input = "=+(){},;";

        // This 'record' is a modern Java way to create a simple, immutable
        // data-holding class. It's the perfect equivalent to the Go
        // anonymous 'struct' in your test.
        record TestCase(TokenType expectedType, String expectedLiteral) {}

        // We create an array of these test cases, just like the Go slice.
        TestCase[] tests = new TestCase[] {
            new TestCase(TokenType.ASSIGN, "="),
            new TestCase(TokenType.PLUS, "+"),
            new TestCase(TokenType.LPAREN, "("),
            new TestCase(TokenType.RPAREN, ")"),
            new TestCase(TokenType.LBRACE, "{"),
            new TestCase(TokenType.RBRACE, "}"),
            new TestCase(TokenType.COMMA, ","),
            new TestCase(TokenType.SEMICOLON, ";"),
            new TestCase(TokenType.EOF, "") // Assuming your EOF literal is an empty string
        };

        // l := New(input)
        Lexer l = new Lexer(input);

        // for i, tt := range tests
        for (int i = 0; i < tests.length; i++) {
            TestCase tt = tests[i];

            // tok := l.NextToken()
            Token tok = l.nextToken();

            // if tok.Type != tt.expectedType
            // We use JUnit's assertion functions instead of 'if' checks.
            assertEquals(
                tt.expectedType(), 
                tok.type(),
                "tests[" + i + "] - tokentype wrong."
            );

            // if tok.Literal != tt.expectedLiteral
            assertEquals(
                tt.expectedLiteral(), 
                tok.literal(),
                "tests[" + i + "] - literal wrong."
            );
        }
    }
}