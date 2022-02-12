import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class OutcomeData {

    private String[] source; // Источник платежа (карты, наличные)
    private String[] category; // Категория покупки
    private String[] subCategory; // Подкатегория покупки
    private double[] outcome; // Сумма покупки

    public OutcomeData(String filePath) throws IOException {
        this.readData(filePath);
    }

    public void readData(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Scanner scanner;
        String lineData;
        List<String> data = new ArrayList<>();
        while ((lineData = reader.readLine()) != null) {
            data.add(lineData);
        }
        int dataLen = data.size();
        reader.close();
        source = new String[dataLen - 1];
        category = new String[dataLen - 1];
        subCategory = new String[dataLen - 1];
        outcome = new double[dataLen - 1];

        int index = 0;
        for (int i = 1; i < dataLen; i++) {
            scanner = new Scanner(data.get(i));
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                String cell = scanner.next();
                if (index == 1)
                    source[i - 1] = cell;
                else if (index == 2)
                    category[i - 1] = cell;
                else if (index == 3)
                    subCategory[i - 1] = cell;
                else if (index == 4) {
                    String[] temp;// = new String[2];
                    temp = cell.split(" ");
                    outcome[i - 1] = Double.parseDouble(temp[0].replace(",", ".").replace(" ", ""));
                }
                index++;
            }
            index = 0;
        }
    }

    public void sourceReport(String filePath) throws IOException {
        SortedSet<String> sourcesList = new TreeSet<String>(Arrays.asList(source));
        String[] sourceArray = sourcesList.toArray(new String[0]);
        double[] sumList = new double[sourceArray.length];
        for (int i = 0; i < sourceArray.length; i++) {
            double sum = 0;
            for (int j = 0; j < source.length; j++) {
                if (sourceArray[i].equals(source[j])) {
                    sum += outcome[j];
                }
            }
            sumList[i] = sum;
        }
        writeToFile("Source of payment", filePath, sourceArray, sumList);

    }

    public void categoryReport(String filePath) throws IOException {
        SortedSet<String> categoryList = new TreeSet<String>(Arrays.asList(category));
        String[] categoryArray = categoryList.toArray(new String[0]);
        double[] sumList = new double[categoryArray.length];
        for (int i = 0; i < categoryArray.length; i++) {
            double sum = 0;
            for (int j = 0; j < category.length; j++) {
                if (categoryArray[i].equals(category[j])) {
                    sum += outcome[j];
                }
            }
            sumList[i] = sum;
        }
        writeToFile("Category of payment", filePath, categoryArray, sumList);
    }

    public void subCategoryReport(String filePath) throws IOException {
        SortedSet<String> subCategoryList = new TreeSet<String>(Arrays.asList(subCategory));
        String[] subCategoryArray = subCategoryList.toArray(new String[0]);
        double[] sumList = new double[subCategoryArray.length];
        for (int i = 0; i < subCategoryArray.length; i++) {
            double sum = 0;
            for (int j = 0; j < subCategory.length; j++) {
                if (subCategoryArray[i].equals(subCategory[j])) {
                    sum += outcome[j];
                }
            }
            sumList[i] = sum;
        }
        writeToFile("Subcategory of payment", filePath, subCategoryArray, sumList);


    }

    private void writeToFile(String mainColumn, String filePath, String[] columnOfReport, double[] sumArray) throws IOException {
        FileWriter writer = new FileWriter(filePath, Charset.forName("cp1251"));
        StringBuilder finalFile = new StringBuilder(mainColumn + ";Sum\n");

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("########.##", symbols);

        for (int i = 0; i < columnOfReport.length; i++) {
            String dataString = columnOfReport[i] + ";" + df.format(sumArray[i]) + "\n";
            finalFile.append(dataString);
        }
        String summary = "Total:;" + df.format(Arrays.stream(sumArray).sum());
        finalFile.append(summary);
        writer.write(finalFile.toString());
        writer.close();
    }
}
