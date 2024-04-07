


Стандартные уровни логирования:

`SEVERE`: серьёзная ошибка

`WARNING`: предупреждение

`INFO`: информационное сообщение

`CONFIG`: конфигурация системы

`FINE`, `FINER`, `FINEST`: детальное логирование

---
[ссылка1](https://sky.pro/wiki/java/nastroyka-formatirovaniya-vyvoda-java-util-logging-v-java/)

* Выводим и в консоль и в файл:

`handlers=java.util.logging.FileHandler,java.util.logging.ConsoleHandler`

* Быстрое решение при помощи **SimpleFormatter**

`java.util.logging.SimpleFormatter.format="%1$tF %1$tT %4$s %2$s %5$s%6$s%n"
`

`%1$tF` `%1$tT` – дата и время события,

`%4$s` – уровень серьёзности сообщения,

`%2$s` – источник сообщения,

`%5$s%6$s` – текст сообщения и информация об исключении.

Данную настройку стоит применять до инициализации каких-либо логгеров в приложении.

---
[ссылка 2](https://ru.stackoverflow.com/questions/975297/java-util-logging-%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%B0-%D1%81-%D0%BD%D0%B5%D1%81%D0%BA%D0%BE%D0%BB%D1%8C%D0%BA%D0%B8%D0%BC%D0%B8-%D0%BB%D0%BE%D0%B3-%D1%84%D0%B0%D0%B9%D0%BB%D0%B0%D0%BC%D0%B8-%D0%BE%D0%B4%D0%BD%D0%BE%D0%B2%D1%80%D0%B5%D0%BC%D0%B5%D0%BD%D0%BD%D0%BE)

---
[ссылка 3](https://www.dxgames.narod.ru/articles/java/logging.htm#loghtml)

---
[ссылка 4](https://sky.pro/wiki/java/nastroyka-formatirovaniya-vyvoda-java-util-logging-v-java/)

---




