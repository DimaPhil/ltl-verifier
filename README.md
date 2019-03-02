# ltl-verifier

Авторы:
* Филиппов Дмитрий, М4138
* Немченко Евгений, М4138

**Grammar**

Грамматика для LTL-формул представлена в файле `src/main/antlr4/Ltl.g4`.
Операторы в порядке убывания приоритета: !, X, F, G, U, R, &&, ||, ->.

Приоритет операторов можно задавать явно с помощью скобок.


**Usage**

Для запуска приложения следует использовать следующую команду в корне проекта:
```bash
java -jar ltl-verifier.jar <automaton file> <file with ltl formulas> [<output file>]
```
Если `output_file` не указан, вывод будет производиться в консоль.

Например, пример запуска для теста из ТЗ:
```bash
java -jar ltl-verifier.jar samples/test1.xstd samples/test1.ltl
```
