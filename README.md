# 🍌 Banana Language - CornHacks 2025

A list-based programming language interpreter written in Java that uses emojis as its primary syntax. Banana is a fun, esoteric programming language designed for the CornHacks 2025 hackathon.

## 📖 Overview

Banana is an emoji-based stack-oriented programming language. Programs are written using various emojis to represent operations, numbers, and control flow. The language features a preprocessor, parser, and interpreter that work together to execute Banana programs.


## 🚀 Features

- **List-based operations**: All operations work on a list data structure
- **Emoji syntax**: Write programs entirely using emojis
- **Number support**: Integers and floating-point numbers
- **Character I/O**: Print characters and numbers
- **Conditional execution**: Equality checks with conditional branching
- **Input handling**: Read emoji input from the console

## 🛠️ Building and Running

### Compilation

Compile the Java source files:

```bash
javac -d . src/bananalang/*.java src/Main.java
```

### Running Programs

**Option 1: Using BananaLang main class**
```bash
java bananalang.BananaLang <file.nana>
```

**Option 2: Using Main class**
```bash
java Main
```
(Note: Modify `Main.java` to point to your desired `.nana` file)

## 📝 Language Syntax

### Stack Operations

| Emoji | Command | Description |
|-------|---------|-------------|
| `🍌` | PUSH_ONE | Push a number onto the stack (see number format below) |
| `🍌🙉` | PUSH_FROM_INDEX | Push element from a specific index onto the stack |

### Arithmetic Operations

| Emoji | Command | Description |
|-------|---------|-------------|
| `🍌🍌` | ADD | Pop two values, add them, push result |
| `🍌🍂` | SUBTRACT | Pop two values, subtract (a - b), push result |
| `🍌🌴` | MULTIPLY | Pop two values, multiply them, push result |
| `🍌🪾` | DIVIDE | Pop two values, divide (a / b), push result |
| `🍌❄️` | MODULUS | Pop two values, compute (a % b), push result |

### I/O Operations

| Emoji | Command | Description |
|-------|---------|-------------|
| `🍌🙈` | PRINT | Pop and print value as number |
| `🍌🙉` | PRINTC | Pop and print value as character (ASCII) |
| `🍌🍌🍌` | PUSH_INPUT | Read emoji input from console and push count |

### Control Flow

| Emoji | Command | Description |
|-------|---------|-------------|
| `🍌❓` | EQUALS | Pop two values, if equal push 1, else skip to next `︶` |
| `︶` | CLOSE | Closing bracket for EQUALS conditional blocks |
| `🍌🍌🍌🍌🍌` | CLEAR | Clear the entire stack |

### Number Format

Numbers are represented using a binary encoding system:

**Integer Format:**
- `🌙` - indicates an integer
- Next emoji: `🍌` (negative) or `🐒` (positive)
- Remaining emojis: binary representation where `🍌` = 1 and `🌙` = 0

**Double Format:**
- `🌙🌙` - indicates a double/float
- Next emoji: `🍌` (negative) or `🐒` (positive)
- Binary digits with `🐒` as decimal point separator

**Examples:**
- `🌙🐒🍌🌙` = positive integer 2 (binary: 10)
- `🌙🍌🍌🌙` = negative integer 2
- `🌙🌙🐒🍌🌙🍌🍌🐒🍌🌙🍌🌙` = 11.10 (binary: 1011.1010)

### Character Encoding

Characters use binary ASCII encoding. The `PRINTC` command interprets the top stack value as an ASCII character code.

## 📁 Project Structure

```
CornHacks2025/
├── src/
│   ├── bananalang/
│   │   ├── BananaInterpreter.java  # Executes parsed commands
│   │   ├── BananaParser.java       # Parses emoji tokens into commands
│   │   ├── BananaPreprocessor.java # Filters and processes input files
│   │   └── BananaLang.java         # Main entry point
│   └── Main.java                   # Alternative entry point
├── *.nana                          # Example Banana program files
└── README.md                       # This file
```

## 💡 Example Programs

### Hello World

See `hello.nana` for a complete "Hello, World!" program example.

### Basic Arithmetic

```banana
🍌 🌙🐒🍌🌙      # Push 2
🍌 🌙🐒🍌🍌      # Push 3
🍌🍌              # Add (result: 5)
🍌🙈              # Print result
```

## 🐛 Error Handling

The interpreter provides clear error messages for:
- Stack underflow (not enough values for operation)
- Invalid number formats
- Index out of bounds
- Unknown tokens

## 📚 Notes

- **Labels**: `🍌` can define labels (space separation then label name made of all 🍌s) - functionality may be in development
- **Whitelist**: The preprocessor filters input to only allow specific emojis, ensuring program validity
- **Stack**: All operations work on a global stack (`ArrayList<Double>`)

## 🤝 Contributing

This project was created for CornHacks 2025. Feel free to extend the language with new features!

## 📄 License

This project is part of the CornHacks 2025 hackathon submission.

## Authors

Caleb Nierman, Jared Obidowski, Krithik Pondicherry, Timofei Prakapchuk
