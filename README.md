Бот для для поиска песен и их текстов с помощью сайта genius.com

Задачи:
1. Реализована работа с ботом через консоль, добавлен функционал поиска песни по ключевым словам
2. Перенести текущую логику в тг
3. По выбранной песни из списка найденных песен по названию вывести её текст или информацию о ней (год, жанр, альбом и т. д.)

### Running locally with maven (dont forget to setup environment vars/config.properties)
```sh
$ git clone https://github.com/complito/oop.git
$ cd oop
$ mvn compile exec:java
```

### Deploying to Heroku
```sh
$ heroku create
$ git push heroku main
$ heroku open
```
