// Входные данные:
// -------------------------------------------------------------------------------------------------
// Калькулятор умеет выполнять операции сложения, вычитания, умножения и деления с двумя числами
// 1) Во входной строке должны только присутствовать символы арифметических действий + - * /
//
// Калькулятор работает с только десятичными или только с римскими числами =>
// 2) Во входной строке должны быть либо
// только символы: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
// только символы I, V, X ( i, v, x ?) => I, II, III, IV, V, VI, VII, VIII, IX, X
// и
// не допускается ввод числа 0 (допускается ли ввод числа с лидирующим 0 -> 01, 02, и так далее ?)
//
// 3) Максимальное значение чисел равно 10 => максимальное результирующее число будет 10 * 10 = 100
//
// -------------------------------------------------------------------------------------------------
// Что необходимо для реализации:
// -------------------------------------------------------------------------------------------------
// На вводе:
// 1) преобразование строки содержащей арабские цифры в число
// 2) преобразование строки содержащей римские цифры в число
// На выводе:
// 1) арабское число просто выводим
// 2) преобразование числа в строку содержащую римские цифры
//
// Выбор переменных:
// 1) строковые переменные для хранения и вывода строк содержащие символы описывающие числа
// 2) числовые переменные для хранения числовых данных соответствующие описанным числовым данным,
//    поскольку максимальное значение результата будет не более 100 диапазон переменной -128 +127
//    будет достаточен
//
// -------------------------------------------------------------------------------------------------
/// !!! Корректировка задания
// Калькулятор умеет работать только с арабскими или римскими цифрами одновременно,
// при вводе пользователем строки вроде 3 + II калькулятор должен выбросить исключение и прекратить свою работу.
// !!! Заменить на
// Калькулятор умеет работать либо только с арабскими, либо только с римскими цифрами,
// при вводе пользователем строки вроде 3 + II калькулятор должен выбросить исключение и прекратить свою работу.
// -------------------------------------------------------------------------------------------------
///
// Шаги обработки
// 1) Обработка введенной строки.
//    - во введенной строке должны быть только символы:
//      0, 1, 2, 3, 4, 5, 6, 7, 8, 9, +, -, *, / и символ "пробела" (0x20) либо
//      I, V, X, i, v, x, +, -, *, / и символ "пробела" (0x20)
//      иначе, сообщение что введены цифры в различных системах исчисления
//    - если присутствуют иные символы:
//      сообщение о том что введены цифры не в арабской и/или римской системе исчисления
//    - во введенной строке должна присутствовать только одна арифметическая операция: либо +, либо -, либо *, либо /
//      наличие двух арифметических операций не допустимо
// 2) Деление введенной строки на операнды.
// 3) Реализация самого калькулятора.

// Для упрощения реализации в части римских чисел создадим строковый массив римских чисел от 1 до 100 включительно,
// и, соответственно порядковый номер элемента массива будет содержать нужную строку, описывающую число в римской
// системе исчисления.
//
// Для римких чисел использовал
// http://graecolatini.bsu.by/htm-different/num-converter-roman.htm
// Интересно:
// https://habr.com/ru/articles/271035/

// =================================================================================================================
// 2024-05-10
// 1 Устранена ошибка обработки входной строки при разбиении на операнды.
//   Воод строки 1+5 завершался исключением -> Needed to input TWO numbers to operate.

import java.util.Scanner;

public class Calc {
    public static void main(String[] args) {
        boolean isRome = false;
        boolean isArabic = false;
        boolean isSpace = false;
        boolean isDisAllowed = false;
        int howMuch = 0;
        int startOffset = 0;
        char toDo = ' ';

        int i; // will use for count

        String[] rome100 = new String[]{
                "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
                "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX",
                "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX",
                "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL",
                "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L",
                "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX",
                "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX",
                "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX",
                "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC",
                "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"
        };

        Scanner calcIn = new Scanner(System.in);
        System.out.print("Calculator input : ");
        String expression = calcIn.nextLine();
        calcIn.close();
        //System.out.printf("Expression: %s\n", expression);

        // Введенную строку всю в uppercase (i, v, x -> I, W, X)
        expression=expression.toUpperCase();

        // Проверка строки на допустимые символы:
        int lenString = expression.length();
        for(i=0; i<lenString; i++) {
            // System.out.printf("%c", expression.charAt(i));  // This is for a debug
            switch (expression.charAt(i)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    isArabic=true;
                    break;
                case 'I':
                case 'V':
                case 'X':
                    isRome=true;
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    // считаем количество символов арифметических операций
                    // и если больше одного, то прерываем с исключением
                    howMuch ++;
                    toDo = expression.charAt(i);
                    // Запомининаем где в строке символ арифметической операции
                    startOffset = i;
                    break;
                case ' ':
                    isSpace=true;
                    break;
                default:
                    // прерывание с исключением: введены не числа или вне заданного диапазона
                    isDisAllowed = true;
            }
        }
        // Исключения:
        // - ничего не введено
        // - введен больше чем один символ допустимой арифметической операции
        // - введены символы не относящиеся к описанию чесел в арабской или рисмкой систем исчисления
        //   или недопустимые символы математических операций
        // - введены цифры и в римской и в арбской системах написания
        //
        //  Первичный разбор исключений
        if( lenString == 0 || isDisAllowed || ( isArabic && isRome ) || ( !isArabic && !isRome ) || howMuch > 1 || howMuch == 0 ) {
            // describe some if's
            //
            if (lenString == 0)
                System.out.println("Nothing to calculate. Please repeat with the numbers.");
            else {
                if (isDisAllowed)
                    System.out.println("Could use 0,1,2,3,4,5,6,7,8,9 and I,V,X and +, -, *, / symbols.");
                if (isArabic && isRome)
                    System.out.println("Could calculate only Rome's or Arabic's numbers.");
                if (howMuch > 1)
                    System.out.println("Can use only one of arifmetic metod, or +, or -, or *, or /.");
                if (howMuch == 0)
                    System.out.println("Need to use at last one of arifmetics metod, or +, or -, or *, or /.");
            }
        }
        else {
            //-----------------------------------------------
            // Продолжение после разбора первичных исключений
            // This is for a debug
            // System.out.println("Dispatch input string: Arabic - "+isArabic+"; Rome - "+isRome+"; ToDO ["+toDo+"]");
            // System.out.println("Is space in the input: "+isSpace);

            // Делим введенную строку на два операнда
            String operand1 = expression.substring(0, startOffset);

            String operand2 = expression.substring(startOffset + 1, lenString);
            // System.out.println("Operand 1: ["+operand1+"]"+" Operand 2: ["+operand2+"]");

            operand1 = operand1.trim();
            operand2 = operand2.trim();
            // Исключения:
            // операнды должны содержать строки с символами чисел
            // Если хотя бы один операнд отсутствует то исключение
            if( operand1.length() == 0 || operand2.length() == 0 ) {
                System.out.println("Needed to input TWO numbers to operate.");
            }
            else {

                // System.out.println("After remove spaces Operand 1: [" + operand1 + "]" + " Operand 2: [" + operand2 + "]");

                int result = 0;
                int oper1 = 0;
                int oper2 = 0;
                if (isArabic) {
                    oper1 = Integer.parseInt(operand1);
                    oper2 = Integer.parseInt(operand2);
                    if (oper1 < 1 || oper1 > 10 || oper2 < 1 || oper2 > 10) {
                        // отрабатываем исключение
                        System.out.println("Values are out of range.");
                    } else {
                        switch (toDo) {
                            case '+':
                                result = oper1 + oper2;
                                break;
                            case '-':
                                result = oper1 - oper2;
                                break;
                            case '*':
                                result = oper1 * oper2;
                                break;
                            case '/':
                                result = oper1 / oper2;
                                break;
                        }
                        System.out.println("Result = " + result);
                    }
                }
                if (isRome) {
                    // Assign
                    for (i = 0; i < 10; i++) {
                        if (operand1.compareTo(rome100[i]) == 0)
                            break;
                    }
                    if (i < 10)
                        oper1 = i + 1;
                    else
                        // exclude: value out of range
                        isDisAllowed = true;

                    for (i = 0; i < 10; i++) {
                        if (operand2.compareTo(rome100[i]) == 0)
                            break;
                    }
                    if (i < 10)
                        oper2 = i + 1;
                    else
                        // exclude: value out of range
                        isDisAllowed = true;
                    if (isDisAllowed) {
                        // отрабатываем исключение
                        System.out.println("Values is out of range.");
                    }

                    if (!isDisAllowed) {
                        switch (toDo) {
                            case '+':
                                result = oper1 + oper2;
                                break;
                            case '-':
                                result = oper1 - oper2;
                                break;
                            case '*':
                                result = oper1 * oper2;
                                break;
                            case '/':
                                result = oper1 / oper2;
                                break;
                        }
                        if (result > 0 && result < 101)
                            System.out.println("Result Rome = " + rome100[result - 1]);
                        else
                            System.out.println("Сalculation of the result is not possible.");
                    }
                }
            }
        }
    }
}

// p.s.
// В данном коде много чего можно еще улучшить
// Цель была - реализация задания
// --------------------------------------------
// В данном задании не вижу необходимости обрабатывать исключения,
// допускаю что при вводе с консоли возможно нужно,
// так как вижу необходимость их применения там, где нельзя что-то обработать
// в теле программы.
// В данном же задании все, ну или почти все можно обработать.
