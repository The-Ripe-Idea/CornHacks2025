# CornHacks2025
Our Cornhacks Project for 2025

https://edu.anarcho-copy.org/Programming%20Languages/Go/writing%20an%20INTERPRETER%20in%20go.pdf

Definition: 
🍌 - define a label, space separation then label name (made of all 🍌s)


NUMBERS:
🌙 / 🌙🌙  - signifies tht the token is a number (int or double)
    🌙 - signifies the token is an int
    🌙🌙 - signifies the token is a double 
Then, based on two's complement, the next number represents negativity
    🍌 signifies a negative number
    🐒 signifies a positive number
Then, represent the number in normal binary with 🍌 = 1 and 🌙 = 0
UNLESS: the number is a double, then use 🐒 as a decimal point and write the numbers seperate

EX:  11.10 == 🌙🌙🐒🍌🌙🍌🍌🐒🍌🌙🍌🌙

EX: 🌙

LETTERS: 
- Uses binary ascii for charcters
- Differentiates between 