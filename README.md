# Задача "Русская рулетка"

Играем в русскую рулетку с шестизарядным револьвером.
В револьвер заряжают не один, а два патрона.
Оба патрона располагают в соседние гнезда, друг за другом, остальные гнёзда пусты.
Барабан прокручивают.
После первого нажатия на спусковой крючок ничего не произошло, первый игрок жив.
Револьвер передаём второму игроку. Надо нажать на спусковой крючок второй раз.
Но вот что лучше, стрелять сразу или прокрутить барабан несколько раз наудачу?
Подразумевается, что второй игрок не собирается проигрывать.
## Задача

1. Ответить на вопрос. Что лучше, нажать на спусковой крючок сразу или крутить барабан? (рассчитать теоретические вероятности исходов).
2. Написать программу, которая смоделирует ситуацию, позволит запустить эксперименты и получить результаты, для сравнения их с теоретическими расчётами.

Существуют 4 возможных варианта расположения патронов после холостого выстрела первого игрока:

1. 11000 
2. 01100 
3. 00110 
4. 00011 

Для второго игрока желательные расположения патронов являются 2 и 4, что дает шанс 50 на 50. Однако, если второй игрок решит прокрутить барабан, то количество возможных расположений патронов увеличится до 6:

1. 011000
2. 001100
3. 000110
4. 000011
5. 100001
6. 110000

Для второго игрока желательные расположения патронов остаются 1 и 3, что дает шанс на победу второго 0.33.

## Запуск
Для запуска приложения, выполните следующую команду:

```bash
docker run -it clairdess/bss_test
```
Если надо запустить приложение без использования docker, то надо:
1. Скачать и распаковать репозиторий
2. Проверить, что установлена JDK
3. Используя терминал, перейдите в папку src и выполнить следующую команду
```bash
java -cp . Revolver.java
```
