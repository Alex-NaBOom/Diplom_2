# Diplom_2
Задание 2: API
Тебе нужно протестировать ручки API для Stellar Burgers.
Пригодится документация API. В ней описаны все ручки сервиса. Тестировать нужно только те, которые указаны в задании. Всё остальное — просто для контекста.
Создание пользователя:
создать уникального пользователя;
создать пользователя, который уже зарегистрирован;
создать пользователя и не заполнить одно из обязательных полей.
Логин пользователя:
логин под существующим пользователем,
логин с неверным логином и паролем.
Изменение данных пользователя:
с авторизацией,
без авторизации,
Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
Создание заказа:
с авторизацией,
без авторизации,
с ингредиентами,
без ингредиентов,
с неверным хешем ингредиентов.
Получение заказов конкретного пользователя:
авторизованный пользователь,
неавторизованный пользователь.
