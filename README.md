# Chat
Чат на сокетах с функционалом кастомных команд
Формат сообщений, которыми обмениваются пользователи имеет вид:
[<Время>|<Имя пользователя>] <Сообщение>
Пример:
```
[12:35:12|Kraken] Hi there
[12:35:34|Kraken] Anybody here
[12:35:45|Kraken] /help
[12:36:02|SYSTEM] Avaliable commands 
				/help - help
				/rename - rename
[12:36:75|Kraken] /help

```
При подключении клиенту необходимо зарегестрироваться командой /register
После этого, ему будет доступен чат.