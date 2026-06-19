public class Main {
    public static void main(String[] args) {
        String testCode = 
            "val correctVar = \"All good\"\n" +  //правильний рядок
            "def testFunc() {\n" +               //правильний рядок
            "  @ \n" +                           //ПОМИЛКА: невідомий символ '@'
            "  val badStr = \"Unclosed \n" +     //ПОМИЛКА: незакритий літерал до перенесення рядка
            "  !correctVar\n" +                  //знову правильний код (сканер відновився)
            "}";
        
        Scanner scanner = new Scanner(testCode);
        Token token;
        
        System.out.println("Lexical Analysis Output (with Error Handling):");
        System.out.println("----------------------------------------------");
        
        do {
            token = scanner.nextToken();
            //якщо це помилка, виводимо її іншим форматом для наочності
            if (token.type == TokenType.ERROR) {
                System.out.println(">>> ALARM: " + token);
            } else {
                System.out.println(token);
            }
        } while (token.type != TokenType.EOF);
    }
}