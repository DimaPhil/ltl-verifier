# ltl-verifier

Авторы:
* Филиппов Дмитрий, М4138
* Немченко Евгений, М4138

Для запуска приложения следует использовать следующую команду в корне проекта:
```bash
java -jar ltl-verifier.jar <automaton file> <file with ltl formulas>
```

Например, пример запуска для теста из ТЗ:
```bash
java -jar ltl-verifier.jar samples/test1.xstd samples/test1.ltl
```