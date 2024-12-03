import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Simplex {
    String[] signs;
    double[] lastColumn;
    double[][] task;
    double[][] table;
    int numberOfVariables;
    int numberOfConstraints;
    boolean minimal;
    int[] basis;
    double[] delta;
    boolean isOptimal = true;

    public Simplex(String fileName) throws IOException {
        readFromFile(fileName);
        getTableFormTask();
        solve();
//        printTable();
//        countDelta();
//        System.out.println(Arrays.toString(delta));
//
//        System.out.println(getRow() + " " + getColumn(getRow()));
//        updateTable(getRow(), getColumn(getRow()));
//        printTable();
//        countDelta();
//        System.out.println(Arrays.toString(delta));
//
//        System.out.println(getRow() + " " + getColumn(getRow()));
//        updateTable(getRow(), getColumn(getRow()));
//        printTable();
//        countDelta();
//        System.out.println(Arrays.toString(delta));
    }

    public void readFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String firstLine = reader.readLine();
        String[] firstLineParts = firstLine.split(" ");
        numberOfVariables = Integer.parseInt(firstLineParts[0]);
        numberOfConstraints = Integer.parseInt(firstLineParts[1]);

        task = new double[numberOfConstraints + 1][numberOfVariables];
        signs = new String[numberOfConstraints + 1];
        lastColumn = new double[numberOfConstraints + 1];

        String line;
        int rowIndex = 0;

        while ((line = reader.readLine()) != null) {

            String[] parts = line.split(" ");

            for (int i = 0; i < numberOfVariables; i++) {
                task[rowIndex][i] = Double.parseDouble(parts[i]);
            }

            signs[rowIndex] = parts[numberOfVariables];

            if (rowIndex < numberOfConstraints) {
                lastColumn[rowIndex] = Double.parseDouble(parts[numberOfVariables + 1]);
            } else {
                minimal = parts[numberOfVariables + 1].equals("min");
                lastColumn[rowIndex] = 0;
            }

            rowIndex++;
        }
    }

    public void getTableFormTask() {
        table = new double[numberOfConstraints + 1][numberOfVariables + numberOfConstraints + 1];
        int counter = 0;
        for (int i = 0; i < numberOfConstraints + 1; i++) {
            double sign = signs[i].equals(">") ? -1 : 1;
            for (int j = 0; j < numberOfVariables; j++) {
                table[i][j] = task[i][j] * sign;
            }
            table[i][numberOfVariables + counter] = 1;
            table[i][numberOfVariables + numberOfConstraints] = lastColumn[i] * sign;
            counter++;
        }
        basis = new int[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++) {
            basis[i] = i + numberOfVariables + 1;
        }
        delta = new double[numberOfConstraints + numberOfVariables + 1];
    }

    public void printTable() {
        System.out.println("-".repeat(12  * (numberOfVariables + numberOfConstraints + 1) + 2)); // Разделитель сверху
        System.out.printf("%-10s", "BASIS");
        for (int i = 0; i < numberOfVariables + numberOfConstraints; i++) {
            System.out.printf("| %-8s ", "X" + (i + 1)); // Заголовки для переменных
        }
        System.out.println("| B        |");

        System.out.println("-".repeat(12  * (numberOfVariables + numberOfConstraints + 1) + 2));

        for (int i = 0; i < numberOfConstraints + 1; i++) {
            if (i == numberOfConstraints) {
                System.out.printf("%-10s", minimal ? "MIN" : "MAX");
            } else {
                System.out.printf("X%-9s", basis[i]);
            }

            for (int j = 0; j < numberOfVariables + numberOfConstraints; j++) {
                System.out.printf("| %-8.2f ", table[i][j]);
            }
            System.out.printf("| %-8.2f |", table[i][numberOfVariables + numberOfConstraints]);
            System.out.println();
        }

        System.out.println("-".repeat(12  * (numberOfVariables + numberOfConstraints + 1) + 2));
    }


    public void updateTable(int row, int column) {
        basis[row] = column + 1;
        double[][] newTable = new double[numberOfConstraints + 1][numberOfVariables + numberOfConstraints + 1];
        double element = table[row][column];
        for (int i = 0; i < numberOfConstraints + numberOfVariables + 1; i++) {
            newTable[row][i] = table[row][i] / element;
        }
        for (int i = 0; i < numberOfConstraints; i++) {
            if (i != row) {
                element = table[i][column];
                for (int j = 0; j < numberOfVariables + numberOfConstraints + 1; j++) {
                    newTable[i][j] = table[i][j] - newTable[row][j] * element;
                }
            }
        }
        for (int i = 0; i < numberOfConstraints + numberOfVariables + 1; i++) {
            newTable[numberOfConstraints][i] = table[numberOfConstraints][i];
        }
        table = newTable;
    }

    public int getRow() {
        int row = -1;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfConstraints; i++) {
            if (table[i][numberOfVariables + numberOfConstraints] < 0 &&
                    Math.abs(table[i][numberOfVariables + numberOfConstraints]) > max) {
                max = Math.abs(table[i][numberOfVariables + numberOfConstraints]);
                row = i;
            }
        }
        return row;
    }

    public int getColumn(int row) {
        int column = -1;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfConstraints + numberOfVariables; i++) {
            if (table[row][i] < 0 &&
                    Math.abs(table[row][i]) > max) {
                max = Math.abs(table[row][i]);
                column = i;
            }
        }
        return column;
    }

    public void countDelta() {
        for (int i = 0; i < numberOfConstraints + numberOfVariables + 1; i++) {
            for (int j = 0; j < numberOfConstraints; j++) {
                delta[i] += table[numberOfConstraints][basis[j] - 1] * table[j][i];
            }
            delta[i] -= table[numberOfConstraints][i];
        }
    }

    public void solve() {
        int count = 0;
        System.out.println("Iterations: " + count);
        printTable();
        while (true) {
            int row = getRow();
            if (row == -1) {
                break;
            }
            int column = getColumn(row);
            System.out.println("Row: " + (row + 1));
            System.out.println("Column: " + (column + 1));
            updateTable(row, column);
            count++;
            System.out.println("Iterations: " + count);
            printTable();
        }
        countDelta();
        for (int i = 0; i < numberOfConstraints + numberOfVariables - 1; i++) {
            System.out.print("Δ" + (i + 1) + " = " + delta[i] + " ");
        }
        System.out.println("ΔB = " + delta[numberOfConstraints + numberOfVariables]);


        for (int i = 0; i < numberOfConstraints + numberOfVariables; i++) {
            if (delta[i] > 0) {
                isOptimal = false;
                break;
            }
        }

        if (isOptimal) {
            System.out.println("Optimal");
            for (int i = 1; i < numberOfVariables + 1; i++) {
                System.out.print("X" + (i) + " = ");
                if (contains(basis, i)) {
                    System.out.print(table[i - 2][numberOfVariables + numberOfConstraints]);
                } else {
                    System.out.print("0");
                }
                System.out.println();
            }
            System.out.println("F = " + delta[numberOfConstraints + numberOfVariables]);
        }
    }

    public static boolean contains(int[] array, int target) {
        for (int num : array) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }
}
