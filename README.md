# Simplex Method Solver

Этот проект реализует решение задачи линейного программирования с использованием симплекс --метода. Он принимает задачу в текстовом формате, анализирует её и находит оптимальное решение.

## Описание

Проект включает в себя программу на языке Java, которая решает задачу линейного программирования симплекс-методом. Входные данные для задачи считываются из текстового файла. Решение задачи осуществляется через пошаговое преобразование симплекс-таблицы до тех пор, пока не будет достигнуто оптимальное решение.

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
<коэффициенты целевой функции> <=> <знак минимизации или максимизации>
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

## Пример работы

Если у вас есть файл `task.txt`, как указано выше, программа выполнит шаги метода симплекс и выведет таблицы и результаты:

```
Table
---------------------------------------------------------------------------------------------------
BASIS     | X1       | X2       | X3       | X4       | X5       | X6       | X7       | B        |
---------------------------------------------------------------------------------------------------
X5        | -1,00    | -1,00    | -0,00    | -4,00    | 1,00     | 0,00     | 0,00     | -26,00   |
X6        | -2,00    | -0,00    | -3,00    | -5,00    | 0,00     | 1,00     | 0,00     | -30,00   |
X7        | -1,00    | -2,00    | -4,00    | -6,00    | 0,00     | 0,00     | 1,00     | -24,00   |
MIN       | 5,00     | 6,00     | 7,00     | 4,00     | 0,00     | 0,00     | 0,00     | 0,00     |
---------------------------------------------------------------------------------------------------
Δ1 = -5,00 Δ2 = -6,00 Δ3 = -7,00 Δ4 = -4,00 Δ5 = 0,00 Δ6 = 0,00 ΔB = 0,00

Pre iterations: 1
There are negative values in column B.
Pivot row: 2
Pivot column: 4
---------------------------------------------------------------------------------------------------
BASIS     | X1       | X2       | X3       | X4       | X5       | X6       | X7       | B        |
---------------------------------------------------------------------------------------------------
X5        | 0,60     | -1,00    | 2,40     | 0,00     | 1,00     | -0,80    | 0,00     | -2,00    |
X4        | 0,40     | 0,00     | 0,60     | 1,00     | -0,00    | -0,20    | -0,00    | 6,00     |
X7        | 1,40     | -2,00    | -0,40    | 0,00     | 0,00     | -1,20    | 1,00     | 12,00    |
MIN       | 5,00     | 6,00     | 7,00     | 4,00     | 0,00     | 0,00     | 0,00     | 0,00     |
---------------------------------------------------------------------------------------------------
Δ1 = -3,40 Δ2 = -6,00 Δ3 = -4,60 Δ4 = 0,00 Δ5 = 0,00 Δ6 = -0,80 ΔB = 24,00

Pre iterations: 2
There are negative values in column B.
Pivot row: 1
Pivot column: 2
---------------------------------------------------------------------------------------------------
BASIS     | X1       | X2       | X3       | X4       | X5       | X6       | X7       | B        |
---------------------------------------------------------------------------------------------------
X2        | -0,60    | 1,00     | -2,40    | -0,00    | -1,00    | 0,80     | -0,00    | 2,00     |
X4        | 0,40     | 0,00     | 0,60     | 1,00     | 0,00     | -0,20    | 0,00     | 6,00     |
X7        | 0,20     | 0,00     | -5,20    | 0,00     | -2,00    | 0,40     | 1,00     | 16,00    |
MIN       | 5,00     | 6,00     | 7,00     | 4,00     | 0,00     | 0,00     | 0,00     | 0,00     |
---------------------------------------------------------------------------------------------------
Δ1 = -7,00 Δ2 = 0,00 Δ3 = -19,00 Δ4 = 0,00 Δ5 = -6,00 Δ6 = 4,00 ΔB = 36,00

Iterations: 1
Pivot row: 1
Pivot column: 6
---------------------------------------------------------------------------------------------------
BASIS     | X1       | X2       | X3       | X4       | X5       | X6       | X7       | B        |
---------------------------------------------------------------------------------------------------
X6        | -0,75    | 1,25     | -3,00    | -0,00    | -1,25    | 1,00     | -0,00    | 2,50     |
X4        | 0,25     | 0,25     | 0,00     | 1,00     | -0,25    | 0,00     | 0,00     | 6,50     |
X7        | 0,50     | -0,50    | -4,00    | 0,00     | -1,50    | 0,00     | 1,00     | 15,00    |
MIN       | 5,00     | 6,00     | 7,00     | 4,00     | 0,00     | 0,00     | 0,00     | 0,00     |
---------------------------------------------------------------------------------------------------
Δ1 = -4,00 Δ2 = -5,00 Δ3 = -7,00 Δ4 = 0,00 Δ5 = -1,00 Δ6 = 0,00 ΔB = 26,00

Optimal
X1 = 0,00
X2 = 0,00
X3 = 0,00
X4 = 6,50
F = 26,00
```

## Описание методов

1. **`readFromFile(String filePath)`** — Считывает данные задачи из текстового файла.
2. **`getTableFormTask()`** — Формирует симплекс-таблицу из данных задачи.
3. **`solve()`** — Основной метод, который выполняет алгоритм симплекс, выводит таблицы на каждом шаге и выводит итоговое решение.
4. **`updateTable(int row, int column)`** — Обновляет симплекс-таблицу после выполнения итерации.
5. **`countDelta()`** — Вычисляет значения $\Delta$ для всех переменных.

## Источник решения
Ход решения был переписан с сайта [Program for You](https://programforyou.ru/calculators/simplex-method), который предоставляет подробное объяснение и примеры применения симплекс-метода для решения задач линейного программирования.
