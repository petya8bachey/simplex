# Simplex Method Solver

Этот проект реализует решение задачи линейного программирования с использованием метода симплекс. Он принимает задачу в текстовом формате, анализирует её и находит оптимальное решение.

## Описание

Проект включает в себя программу на языке Java, которая решает задачу линейного программирования методом симплекс. Входные данные для задачи считываются из текстового файла. Решение задачи осуществляется через пошаговое преобразование симплекс-таблицы до тех пор, пока не будет достигнуто оптимальное решение.

## Структура проекта

Проект состоит из следующих файлов:

- **Main.java** — основной файл, который запускает решение задачи с помощью класса Simplex.
- **Simplex.java** — основной класс, реализующий метод симплекс. Он включает методы для считывания данных, формирования симплекс-таблицы, выполнения итераций и нахождения оптимального решения.
- **task.txt** — файл, содержащий входные данные задачи линейного программирования.

## Формат файла `task.txt`

Файл `task.txt` должен быть отформатирован следующим образом:

```
<количество переменных> <количество ограничений>
<коэффициенты ограничения 1> <знак ограничения 1> <свободный член 1>
<коэффициенты ограничения 2> <знак ограничения 2> <свободный член 2>
...
<коэффициенты функции цели> <знак минимизации или максимизации>
```

Пример:

```
4 3
1 1 0 4 > 26
2 0 3 5 > 30
1 2 4 6 > 24
5 6 7 4 = min
```

Это означает, что у нас есть 4 переменных и 3 ограничения. Функция цели минимизируется (min), а также указаны коэффициенты для каждого ограничения и для функции цели.

## Как запустить

1. Склонируйте репозиторий или скачайте проект.
2. Поместите файл `task.txt` с вашей задачей в папку проекта.
3. Скомпилируйте и запустите проект:

    ```bash
    javac Main.java Simplex.java
    java Main
    ```

4. Программа выведет симплекс-таблицу и решение задачи, если оно существует.

## Пример работы

Если у вас есть файл `task.txt`, как указано выше, программа выполнит шаги метода симплекс и выведет таблицы и результаты:

```
Table
------------------------------------------------------------
BASIS | X1        | X2        | X3        | X4        | B        |
------------------------------------------------------------
X1     | 1.00      | 0.00      | 0.00      | 4.00      | 26.00    |
X2     | 0.00      | 1.00      | 0.00      | 5.00      | 30.00    |
X3     | 0.00      | 0.00      | 1.00      | 6.00      | 24.00    |
MIN    | 5.00      | 6.00      | 7.00      | 4.00      | 0.00     |
------------------------------------------------------------
Δ1 = -2.00 Δ2 = 1.00 Δ3 = 0.00 Δ4 = -1.00 ΔB = -10.00
...

Optimal
X1 = 10.00
X2 = 0.00
X3 = 5.00
F = 20.00
```

## Описание методов

1. **`readFromFile(String filePath)`** — Считывает данные задачи из текстового файла.
2. **`getTableFormTask()`** — Формирует симплекс-таблицу из данных задачи.
3. **`solve()`** — Основной метод, который выполняет алгоритм симплекс, выводит таблицы на каждом шаге и выводит итоговое решение.
4. **`updateTable(int row, int column)`** — Обновляет симплекс-таблицу после выполнения итерации.
5. **`countDelta()`** — Вычисляет значения $\Delta$ для всех переменных.
