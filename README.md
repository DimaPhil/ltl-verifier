# ltl-verifier

Авторы:
* Филиппов Дмитрий, М4138
* Немченко Евгений, М4138

**Grammar**

Грамматика для LTL-формул представлена в файле `src/main/antlr4/Ltl.g4`.
Операторы в порядке убывания приоритета: `!`, `X`, `F`, `G`, `U`, `R`, `&&`, `||`, `->`.

Также поддерживается задание приоритета операций с помощью скобок.

**Usage**

Для запуска приложения следует использовать следующую команду в корне проекта:
```bash
java -jar ltl-verifier.jar <automaton file> <file with ltl formulas> [<output file>]
```
Если `<output file>` не указан, вывод будет производиться на консоль.

Например, пример запуска для теста из [ТЗ](https://docs.google.com/document/d/1nUaRnyy4cL5SgwDCfFBZLZiXETVISTsDWTk6-gUnEsk/edit#heading=h.2y0fnvcw1nma):
```bash
java -jar ltl-verifier.jar samples/test1.xstd samples/test1.ltl
```

Также в `samples` есть примеры для еще двух автоматов из [яндекс диска](https://yadi.sk/d/TORBBQ653EHfsV).
