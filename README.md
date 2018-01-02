# Open-bs Android Application Template

Это шаблон Android проекта.

## In App Billing

 - Папка aidl
 - Пакет iabUtils
 - Разрешение в манифесте
    `<uses-permission android:name="com.android.vending.BILLING" />`
 - Activity BuyActivity
 - Public License Key (смотреть в активити)

## GCM

 - Зависимость в gradle файле верхнего уровня
    `classpath 'com.google.gms:google-services:2.0.0-beta5'`
 - Плагин в gradle фале уровня модуля
    `apply plugin: 'com.google.gms.google-services'`
 - Зависимость в gradle фале уровня модуля
    `compile "com.google.android.gms:play-services-gcm:$PLAY_SERVICES_VERSION"`
 - Пакет *gcmServices*
 - Файл настроек *google-services.json*

 * Необходимо содержать все зависимости в самом актуальном состоянии! При подключении обязательно
 обязательно проверить наличие более свежих пакетов. Последние версии пакетов могут конфликтовать.

 * В проекте подключен ключ для аккаутна p.nikolaev@open-bs.ru

## REST

 - Интерфейс `data.remote.RestService`
 
## DB - GreenDAO
 
 - Module Generator
 - Main Classes and Template.

