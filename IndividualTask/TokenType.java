public enum TokenType {
    //ключові слова
    KEYWORD,
    
    //ідентифікатори (змінні, назви функцій) та літерали
    IDENTIFIER, 
    STRING_LITERAL,
    
    ASSIGN,       //присвоєння =
    PREFIX_OP,    //префіксні оператори !, ~, +, -
    
    LPAREN, RPAREN,         // ( )
    LBRACE, RBRACE,         // { }
    LBRACKET, RBRACKET,     // [ ]
    
    DELIMITER,              //обмежувачі: ,, ;, перенесення рядків
    
    //службові типи
    EOF,                    //кінець файлу
    ERROR                   //невідомий символ
}