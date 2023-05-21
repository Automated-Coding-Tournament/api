package com.pvp.codingtournament.business;

public class Constants {
    public static final String javaTaskBaseCode =
            "public class classNamePlaceholder {\n" +
            "\n" +
            "    public static void main(String[] args){\n" +
            "        Scanner scanner = new Scanner(System.in); \n" +
            "        String[] arguments = scanner.nextLine().split(\";\"); \n" +
            "        System.out.println(methodNamePlaceholder(inputPlaceholder));\n" +
            "    }\n" +
            "\n" +
            "    public static returnTypePlaceholder methodNamePlaceholder(methodArgumentPlaceholder) {\n" +
            "        return null;\n" +
            "    }\n" +
            "}";

    public static final String pythonTaskBaseCode ="\n" +
            "for line in sys.stdin:\n" +
            "   if 'q' == line.rstrip():\n" +
            "       break\n" +
            "   arguments = line.split(';')\n" +
            "\n" +
            "def methodNamePlaceholder(methodArgumentPlaceholder):\n" +
            "   return null\n" +
            "\n" +
            "print(methodNamePlaceholder(inputPlaceholder))";
}
