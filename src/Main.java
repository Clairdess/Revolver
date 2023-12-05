import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Scanner;

/**
 * <p>1. Вероятность успешного исхода для первого игрока составляет 4/6 (четыре незаряженных слота из 6), 66.(6)%
 * <p>2. Варианты для второго игрока
 * <p>2.1. Вероятность успешного исхода для случая "стрелять сразу" составляет 3/4, 75%.
 * <p>2.2. Вероятность успешного исхода для случая "вращать" будет полностью соответствовать вероятности успешного исхода для первого игрока - 4/6, 66.(6)%
 */

public class Main {
    /**
     * Объект Random, предназначенный для генерации случайных чисел.
     * Используется в программе для случайного выбора победителя при вращении барабана.
     */
    private static final Random random = new Random();
    /**
     * Объект Scanner, предназначенный для чтения данных из стандартного ввода (клавиатуры).
     * Используется для ввода количества экспериментов от пользователя в программе.
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Точка входа в программу. Запрашивает у пользователя количество экспериментов и выводит результаты двух
     * сценариев: сразу нажатие на курок и вращение барабана. Результаты выводятся в консоль в виде количества
     * побед каждого игрока и процента успеха.
     *
     * @param args Аргументы командной строки (не используются).
     */

    public static void main(String[] args) {
        System.out.println("Введите количество экспериментов");
        BigDecimal numbersOfExperiments = getNumberOfExperiments();

        System.out.println("Количество экспериментов " + numbersOfExperiments.longValue());
        calculate(numbersOfExperiments);
    }

    /**
     * Запрашивает у пользователя количество экспериментов для проведения.
     * Если введенное значение является целым числом, оно преобразуется в объект BigDecimal и возвращается.
     * Если значение не целое, то у него удаляется дробная часть.
     * Если введено не число, выводится сообщение об ошибке,
     * и возвращается значение по умолчанию - 10000 экспериментов.
     *
     * @return Объект BigDecimal, представляющий количество экспериментов.
     */
    private static BigDecimal getNumberOfExperiments() {
        BigDecimal numbers;
        try {
            numbers = new BigDecimal(sc.next()).setScale(0, RoundingMode.DOWN);
            if (numbers.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
            System.out.println("Ошибка в вводе числа. Число экспериментов будет равно 10000");
            return BigDecimal.valueOf(10000);
        }
        return numbers;
    }

    /**
     * Выполняет серию экспериментов, оценивая выживаемость игроков в игре с револьвером.
     *
     * @param numbersOfExperiments Количество экспериментов, которые будут проведены.
     */
    private static void calculate(BigDecimal numbersOfExperiments) {
        // Инициализация переменных для подсчета побед каждого игрока
        BigDecimal firstPlayerWins = BigDecimal.ZERO;
        BigDecimal secondPlayerWinsSpin = BigDecimal.ZERO;
        BigDecimal secondPlayerWinsShoot = BigDecimal.ZERO;

        // Проведение указанного числа экспериментов
        for (int i = 0; i < numbersOfExperiments.longValue(); i++) {
            // Генерация случайного числа для револьвера
            int seed = random.nextInt(6);
            Revolver revolver = new Revolver(seed);

            // Первый выстрел
            if (!revolver.shoot()) {
                firstPlayerWins = firstPlayerWins.add(BigDecimal.ONE);
            } else {
                continue;
            }

            // Второй выстрел
            if (!revolver.shoot()) {
                secondPlayerWinsShoot = secondPlayerWinsShoot.add(BigDecimal.ONE);
            }

            // Вращение барабана и третий выстрел
            revolver.spin();
            if (!revolver.shoot()) {
                secondPlayerWinsSpin = secondPlayerWinsSpin.add(BigDecimal.ONE);
            }
        }

        // Расчет процентов побед и вывод результатов
        BigDecimal percentAll = calculateWinPercentage(firstPlayerWins, numbersOfExperiments);
        BigDecimal percentShoot = calculateWinPercentage(secondPlayerWinsShoot, firstPlayerWins);
        BigDecimal percentSpin = calculateWinPercentage(secondPlayerWinsSpin, firstPlayerWins);

        String firstInfo = "\tПервый выжил раз " + firstPlayerWins + ". Процент = " + percentAll + " от " + numbersOfExperiments;
        String secondInfo = "\tВторой выжил раз для случая стрелять " + secondPlayerWinsShoot + ". Процент = " + percentShoot + " от " + firstPlayerWins;
        String thirdInfo = "\tВторой выжил раз для случая крутить " + secondPlayerWinsSpin + ". Процент = " + percentSpin + " от " + firstPlayerWins;

        // Вывод информации о победах
        System.out.println(firstInfo);
        System.out.println(secondInfo);
        System.out.println(thirdInfo);
    }


    /**
     * Вычисляет процент успеха на основе количества побед и общего числа попыток.
     * Если входные данные допустимы (проверяется методом {@link #isValidInput(BigDecimal, BigDecimal)}),
     * то процент успеха вычисляется как (wins * 100) / total с округлением вверх до ближайшего целого числа.
     *
     * @param wins  Количество побед.
     * @param total Общее количество попыток или событий.
     * @return Процент успеха в виде объекта BigDecimal.
     */
    private static BigDecimal calculateWinPercentage(BigDecimal wins, BigDecimal total) {
        isValidInput(wins, total);
        return wins.
                multiply(BigDecimal.valueOf(100)).
                divide(total, RoundingMode.HALF_UP);
    }

    /**
     * Проверяет, являются ли входные данные для вычисления процента побед допустимыми.
     * Входные данные считаются допустимыми, если:
     * - Оба параметра wins и total не являются null.
     * - Параметра total больше нуля.
     *
     * @param wins  Количество побед.
     * @param total Общее количество попыток или событий.
     * @return true, если входные данные допустимы; в противном случае - false.
     * @throws NullPointerException     если хотя бы один из параметров (wins или total) равен null.
     * @throws IllegalArgumentException если total или wins меньше, или равны нулю.
     */
    private static boolean isValidInput(BigDecimal wins, BigDecimal total) {
        if (wins != null && total != null) {
            if (total.compareTo(BigDecimal.ZERO) >= 0) {
                return true;
            }
            throw new IllegalArgumentException("Недопустимые входные данные: total должен быть неотрицательными");
        } else
            throw new IllegalArgumentException("Значения wins и/или total не заданы. NPE");
    }
}

/**
 * Класс Revolver представляет собой простой револьвер с вращающимся барабаном, который можно стрелять и крутить.
 */
class Revolver {
    /**
     * Текущая позиция указателя барабана.
     */
    private int pointer = 0;
    /**
     * Массив, представляющий патроны в барабане револьвера.
     * 1 указывает на заряженный патрон, 0 - на пустую камеру.
     */
    private final int[] bullets = new int[]{0, 0, 0, 0, 0, 0};

    /**
     * Выстреливает патрон из револьвера, перемещая указатель барабана на следующую камеру.
     *
     * @return true, если выстрел произведен (есть патрон в камере), в противном случае - false.
     */
    public boolean shoot() {
        if (pointer == bullets.length - 1) {
            pointer = 0;
            return bullets[bullets.length - 1] == 1;
        } else {
            pointer++;
            return bullets[pointer] == 1;
        }
    }

    /**
     * Вращает барабан револьвера, случайным образом.
     */
    public void spin() {
        Random random = new Random();
        int seed = random.nextInt(6);
        if (seed == 5) {
            bullets[seed] = 1;
            bullets[0] = 1;
        } else {
            bullets[seed] = 1;
            bullets[seed + 1] = 1;
        }
        pointer = 0;
    }

    /**
     * Инициализирует распределение патронов в барабане револьвера на основе заданного сида.
     *
     * @param seed Сид, используемое для определения распределения патронов.
     */
    private void fillRandom(int seed) {
        if (seed == 5) {
            bullets[seed] = 1;
            bullets[0] = 1;
        } else {
            bullets[seed] = 1;
            bullets[seed + 1] = 1;
        }
    }
    /**
     * Создает новый объект револьвера с установленным начальным распределением патронов на основе заданного сида.
     *
     * @param seed Сид, определяющее начальное распределение патронов.
     */
    public Revolver(int seed) {
        fillRandom(seed);
    }
}