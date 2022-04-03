import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;

public class OutcomeData {

    private ArrayList<String> source = new ArrayList<>(); // Источник платежа (карты, наличные)
    private ArrayList<String> category = new ArrayList<>(); // Категория покупки
    private ArrayList<String> subCategory = new ArrayList<>(); // Подкатегория покупки
    private ArrayList<Double> outcome = new ArrayList<>(); // Сумма покупки
    private LocalDate[] datesArray; // Отсортированный массив с датами расходов, созданный из множества.

    public OutcomeData(String filePath) {
        this.readData(filePath);
    }

    public void readData(String filePath) {
        try {
            Path path = Paths.get(filePath);
            List<String> data = Files.readAllLines(path);
            SortedSet<LocalDate> datesSet = new TreeSet<LocalDate>();
            for (String lineData : data) {
                String[] strArray = lineData.split(";");
                String[] strDate = strArray[0].split("\\.");
                LocalDate date = LocalDate.of(Integer.parseInt(strDate[2]), Integer.parseInt(strDate[1]), Integer.parseInt(strDate[0]));
                datesSet.add(date);
                source.add(strArray[1]);
                category.add(strArray[2]);
                subCategory.add(strArray[3]);
                String[] cost = strArray[4].split(" ");
                outcome.add(Double.parseDouble(cost[0].replace(",", ".").replace("\u00A0", "")));
            }
            datesArray = datesSet.toArray(new LocalDate[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sourceReport(String filePath) {
        SortedSet<String> sourcesList = new TreeSet<String>(source);
        String[] sourceArray = sourcesList.toArray(new String[0]);
        double[] sumList = new double[sourceArray.length];
        for (int i = 0; i < sourceArray.length; i++) {
            double sum = 0;
            for (int j = 0; j < source.size(); j++) {
                if (sourceArray[i].equals(source.get(j))) {
                    sum += outcome.get(j);
                }
            }
            sumList[i] = sum;
        }
        writeToFile("Source of payment", filePath, sourceArray, sumList);
    }

    public void categoryReport(String filePath) {
        SortedSet<String> categoryList = new TreeSet<String>(category);
        String[] categoryArray = categoryList.toArray(new String[0]);
        double[] sumList = new double[categoryArray.length];
        for (int i = 0; i < categoryArray.length; i++) {
            double sum = 0;
            for (int j = 0; j < category.size(); j++) {
                if (categoryArray[i].equals(category.get(j))) {
                    sum += outcome.get(j);
                }
            }
            sumList[i] = sum;
        }
        writeToFile("Category of payment", filePath, categoryArray, sumList);
    }

    public void subCategoryReport(String filePath) {
        SortedSet<String> subCategoryList = new TreeSet<String>(subCategory);
        String[] subCategoryArray = subCategoryList.toArray(new String[0]);
        double[] sumList = new double[subCategoryArray.length];
        for (int i = 0; i < subCategoryArray.length; i++) {
            double sum = 0;
            for (int j = 0; j < subCategory.size(); j++) {
                if (subCategoryArray[i].equals(subCategory.get(j))) {
                    sum += outcome.get(j);
                }
            }
            sumList[i] = sum;
        }
        writeToFile("Subcategory of payment", filePath, subCategoryArray, sumList);


    }

    private void writeToFile(String mainColumn, String filePath, String[] columnOfReport, double[] sumArray) {
        try (FileWriter writer = new FileWriter(filePath, Charset.forName("cp1251"))) {
            StringBuilder finalFile = new StringBuilder("Report from " + datesArray[0].toString() + " to " +
                    datesArray[datesArray.length - 1] + "\n" + mainColumn + ";Sum\n");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
