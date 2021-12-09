Бот для для поиска песен и их текстов с помощью сайта genius.com

Задачи:
1. Реализована работа с ботом через консоль, добавлен функционал поиска песни по ключевым словам
2. Перенести текущую логику в тг
3. По выбранной песни из списка найденных песен по названию вывести её текст или информацию о ней (год, жанр, альбом и т. д.)
4. Вывод топ 10 популярных песен указанного артиста

# About
Java-bot which works with Genius.com API <br />
Use /help to detailed info about commands

## Setting up
```sh
$ git clone https://github.com/complito/oop.git
$ cd oop
```
You can either use resources/config.properties or environment variables
1. For resources/config.properties <br />
  Uncomment 17-28 lines of getBotToken() in src/ru/oop/BotIO.java and comment line 30 <br />
  Uncomment 37-48 lines of getGeniusToken() in src/ru/oop/BotLogic.java and comment line 50 <br />
  Go to resources/config.properties and set up your telegram bot token as botToken and your Genius token as geniusToken <br />
2. For environment variables <br />
  Just set up your telegram bot token as botToken and your Genius token as geniusToken

### Running test with maven
```sh
$ mvn clean test
```

### Running locally with maven
```sh
$ mvn clean compile exec:java
```

### Deploying to Heroku
```sh
$ heroku create
$ git push heroku main
$ heroku open
```
