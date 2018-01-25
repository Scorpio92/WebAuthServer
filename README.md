# WebAuthServer
Сервер авторизации на Java

Наша Вика тут: https://github.com/mpgp/WebAuthServer/wiki

## Кратенькая инструкция как развернуть

### Установка и настройка MySQL:
```
sudo apt-get install mysql-server
sudo apt-get install mysql-client
sudo apt-get install mysql-workbench
```
**Остановите mysqld:**
```
sudo service mysql stop
```
**Запустите mysqld с параметрами --skip-grant-tables --user=root:**
```
sudo mysqld --skip-grant-tables --user=root
```
**Если команда не сработает, добавьте строку «skip-grant-tables» в секцию «[mysqld]» файла /etc/mysql/mysql.conf.d/mysqld.cnf. Затем выполните**
```
sudo service mysql restart
```
После выполнения операций удалите эту строку.

**Подключитесь к MySQL-серверу командой:**
```
mysql -u root
```
**Обновите пароль для root'a:**
```
UPDATE mysql.user SET authentication_string=PASSWORD('<новый пароль>'), plugin='mysql_native_password' WHERE User='root' AND Host='localhost';
FLUSH PRIVILEGES;
```
**Для MySQL версий < 5.7:**
```
UPDATE mysql.user SET Password=PASSWORD('<новый пароль>') WHERE User='root';
FLUSH PRIVILEGES;
```
**Перезапустите демона:**
```
sudo service mysql restart
```
**Теперь можете проверить вход под root с новым паролем:**
```
mysql -u root -p
```

### Создание БД
```
mysql -u root -p
CREATE DATABASE `chat` /*!40100 DEFAULT CHARACTER SET latin1 */;
GRANT ALL ON chat.* TO 'server' IDENTIFIED BY 'server';
quit;
```

### Скрипты создания таблиц
```
mysql -u server -p chat
```
```
CREATE TABLE `Accounts` (
  `accountId` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(20) NOT NULL,
  `authToken` varchar(128) NOT NULL,
  `tokenCreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`accountId`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`),
  UNIQUE KEY `authToken_UNIQUE` (`authToken`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
```
```
CREATE TABLE `AuthInfo` (
  `authToken` varchar(128) NOT NULL,
  `sessionKeyId` varchar(40) NOT NULL,
  `sessionKey` varchar(128) NOT NULL,
  `IV` varchar(128) NOT NULL,
  `active` int(1) NOT NULL,
  `keyCreateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`authToken`),
  UNIQUE KEY `sessionKeyId_UNIQUE` (`sessionKeyId`),
  UNIQUE KEY `sessionKey_UNIQUE` (`sessionKey`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
```
```
CREATE TABLE `ServerKeys` (
  `pairId` varchar(40) NOT NULL,
  `publicKey` text NOT NULL,
  `privateKey` text NOT NULL,
  PRIMARY KEY (`pairId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
```

### Как завести запорожец

**Создать на сервере папку server и скопировать в нее 4 библиотеки:**

*bcprov-jdk15on-159.jar*

*gson-2.8.2.jar*

*mysql-connector-java-5.1.23-bin.jar*

*chatserver.jar*

**Запустить сервер командой:**
```
java -cp chatserver.jar:/home/ubuntu/server/*: ru.scorpio92.chatserver.AuthServer
```
