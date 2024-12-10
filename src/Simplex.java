import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Simplex {
    private String[] signs;
    private double[] lastColumn;
    private double[][] task;
    private double[][] table;
    private int numberOfVariables;
    private int numberOfConstraints;
    private int totalNumber;
    private boolean minimal;
    private int[] basis;
    private double[] delta;

    public Simplex(String fileName) {
        readFromFile(fileName);
        getTableFormTask();
        solve();
    }

    public void readFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String[] firstLineParts = reader.readLine().split(" ");
            numberOfVariables = Integer.parseInt(firstLineParts[0]);
            numberOfConstraints = Integer.parseInt(firstLineParts[1]);
            totalNumber = numberOfVariables + numberOfConstraints;

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
                lastColumn[rowIndex] = rowIndex < numberOfConstraints
                        ? Double.parseDouble(parts[numberOfVariables + 1])
                        : 0;
                if (rowIndex == numberOfConstraints) {
                    minimal = parts[numberOfVariables + 1].equals("min");
                }

                rowIndex++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getTableFormTask() {
        table = new double[numberOfConstraints + 1][totalNumber + 1];
        int counter = 0;
        for (int i = 0; i < numberOfConstraints + 1; i++) {
            double sign = signs[i].equals(">") ? -1 : 1;
            System.arraycopy(task[i], 0, table[i], 0, numberOfVariables);
            for (int j = 0; j < numberOfVariables; j++) {
                table[i][j] *= sign;
            }
            table[i][numberOfVariables + counter] = 1;
            table[i][totalNumber] = lastColumn[i] * sign;
            counter++;
        }
        basis = new int[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++) {
            basis[i] = i + numberOfVariables + 1;
        }
        delta = new double[totalNumber + 1];
    }

    public void printTable() {
        System.out.println("-".repeat(12 * (totalNumber + 1) + 3));
        System.out.printf("%-10s", "BASIS");
        for (int i = 0; i < totalNumber; i++) {
            System.out.printf("| %-8s ", "X" + (i + 1));
        }
        System.out.println("| B        |");

        System.out.println("-".repeat(12 * (totalNumber + 1) + 3));

        for (int i = 0; i < numberOfConstraints + 1; i++) {
            if (i == numberOfConstraints) {
                System.out.printf("%-10s", minimal ? "MIN" : "MAX");
            } else {
                System.out.printf("X%-9s", basis[i]);
            }

            for (int j = 0; j < totalNumber; j++) {
                System.out.printf("| %-8.2f ", table[i][j]);
            }
            System.out.printf("| %-8.2f |", table[i][totalNumber]);
            System.out.println();
        }

        System.out.println("-".repeat(12 * (totalNumber + 1) + 3));
        printDelta();
        System.out.println();
    }

    public void updateTable(int row, int column) {
        basis[row] = column + 1;
        double element = table[row][column];
        for (int i = 0; i < table[row].length; i++) {
            table[row][i] /= element;
        }

        for (int i = 0; i < numberOfConstraints; i++) {
            if (i != row) {
                element = table[i][column];
                for (int j = 0; j < table[i].length; j++) {
                    table[i][j] -= table[row][j] * element;
                }
            }
        }
    }

    public int getRow() {
        int row = -1;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < numberOfConstraints; i++) {
            if (table[i][totalNumber] < 0 &&
                    Math.abs(table[i][totalNumber]) > max) {
                max = Math.abs(table[i][totalNumber]);
                row = i;
            }
        }
        return row;
    }

    public int getColumn(int row) {
        int column = -1;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < totalNumber; i++) {
            if (table[row][i] < 0 && Math.abs(table[row][i]) > max) {
                max = Math.abs(table[row][i]);
                column = i;
            }
        }
        return column;
    }

    public void countDelta() {
        for (int i = 0; i < totalNumber + 1; i++) {
            delta[i] = 0;
        }

        for (int i = 0; i < totalNumber + 1; i++) {
            for (int j = 0; j < numberOfConstraints; j++) {
                delta[i] += table[numberOfConstraints][basis[j] - 1] * table[j][i];
            }
            delta[i] -= table[numberOfConstraints][i];
        }
    }

    private void solve() {
        System.out.println("Table");
        printTable();
        int count = 1;
        int row;
        int column;
        while (!allPositive(getLastColumnFromTable())) {
            System.out.println("Pre iterations: " + count);
            System.out.println("There are negative values in column B.");
            row = getRow();
            column = getColumn(row);
            System.out.println("Pivot row: " + (row + 1));
            System.out.println("Pivot column: " + (column + 1));
            updateTable(row, column);
            printTable();
            count++;
        }
        count = 1;
        while (true) {
            if (minimal) {
                if (allNegative(Arrays.copyOf(delta, totalNumber))) break;
                column = getColumnMin();
                row = getRowMin(column);
            } else {
                if (allPositive(Arrays.copyOf(delta, totalNumber))) break;
                column = getColumnMax();
                row = getRowMax(column);
            }
            System.out.println("Iterations: " + count);
            System.out.println("Pivot row: " + (row + 1));
            System.out.println("Pivot column: " + (column + 1));
            updateTable(row, column);
            printTable();
            count++;
        }

        System.out.println("Optimal");
        for (int i = 1; i < numberOfVariables + 1; i++) {
            System.out.print("X" + i + " = ");
            System.out.printf("%.2f%n",contains(basis, i) ? table[getBasicIndex(i)][totalNumber] : 0);
        }
        System.out.printf("F = %.2f%n", delta[totalNumber]);
    }

    private int getColumnMax() {
        int column = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < totalNumber; i++) {
            if (delta[i] < min) {
                min = delta[i];
                column = i;
            }
        }
        return column;
    }

    private int getRowMax(int column) {
        int row = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < numberOfConstraints; i++) {
            if (table[i][column] > 0 &&
                    table[i][totalNumber] / table[i][column] < min) {
                min = table[i][totalNumber] / table[i][column];
                row = i;
            }
        }
        return row;
    }

    private int getColumnMin() {
        int column = -1;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < totalNumber; i++) {
            if (delta[i] > max) {
                max = delta[i];
                column = i;
            }
        }
        return column;
    }

    private int getRowMin(int column) {
        return getRowMax(column);
    }

    private void printDelta() {
        countDelta();
        for (int i = 0; i < totalNumber - 1; i++) {
            System.out.printf("Δ%d = %.2f ", i + 1, delta[i]);
        }
        System.out.printf("ΔB = %.2f%n", delta[totalNumber]);
    }

    private boolean contains(int[] array, int target) {
        for (int num : array) {
            if (num == target) {
                return true;
            }
        }
        return false;
    }

    private boolean allPositive(double[] array) {
        for (double num : array) {
            if (num < 0) return false;
        }
        return true;
    }

    private int getBasicIndex(int i) {
        for (int j = 0; j < numberOfConstraints; j++) {
            if (basis[j] == i) return j;
        }
        return -1;
    }

    private boolean allNegative(double[] array) {
        for (double num : array) {
            if (num > 0) return false;
        }
        return true;
    }

    private double[] getLastColumnFromTable() {
        double[] result = new double[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++) {
            result[i] = table[i][totalNumber];
        }
        return result;
    }
}
