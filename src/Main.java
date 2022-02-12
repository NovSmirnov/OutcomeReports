import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Перечень консольных команд для вывода соотвествующих отчётов в файл");
        System.out.println("source_rep - выводит отчёт по затратам с каждого источника платежа,");
        System.out.println("cat_rep - выводит отчёт по затратам на каждую категорию товаров и услуг,");
        System.out.println("sub_rep - выводит отчёт по затратам на каждую подкатегорию товаров и услуг,");
        System.out.println("all_rep - выводит все представленные выше отчёты" + "\n");


        Scanner scanner;
        String command;
        do {
            System.out.println("Введите команду:");
            scanner = new Scanner(System.in);

        } while (!((command = scanner.next()).equals("source_rep") || command.equals("cat_rep") || command.equals("sub_rep")
                || command.equals("all_rep")));
        OutcomeData data = new OutcomeData("src/outcomeReport/outcome082017.txt");
        if (command.equals("source_rep")) {
            data.sourceReport("src/outcomeReport/sourceReport.csv");
        } else if (command.equals("cat_rep")) {
            data.categoryReport("src/outcomeReport/categoryReport.csv");
        } else if (command.equals("sub_rep")) {
            data.subCategoryReport("src/outcomeReport/subCategoryReport.csv");
        } else if (command.equals("all_rep")) {
            data.sourceReport("src/outcomeReport/sourceReport.csv");
            data.categoryReport("src/outcomeReport/categoryReport.csv");
            data.subCategoryReport("src/outcomeReport/subCategoryReport.csv");
        }
        System.out.println("Отчёт создан");
    }
}
