# language: ru
Функция: Получение продолжительности выполнения сборок

  Сценарий: Получение продолжительности сборок за месяц
    Дано выбраны параметры отображения за период "MONTH" и с флагом отображения упавших сборок "true"
    Когда выбран статистический показатель "SUM"
    Тогда отбираются успешные и упавшие сборки за месяц с вычислением суммарного времени

  Сценарий: Получение среднего продолжительности сборок за квартал в JSON
    Дано сформировано 4 запуска задания
    Когда выбраны параметры отображения за период "QUARTER" и с флагом отображения упавших сборок "0" и статистический показатель "AVG"
    Тогда отбираются успешные сборки за последние 3 месяца с вычислением среднего времени
