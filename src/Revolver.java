import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.Scanner;

/**
 * Класс, представляющий 6-зарядный револьвер.
 *
 * <p>Револьвер заряжается 2 патронами, которые идут друг за другом.
 * Участвуют 2 человека в игре.
 *
 * <p>Первый игрок нажимает на курок, но выстрел не происходит.
 * Второй игрок должен принять решение: нажать на курок или прокрутить барабан несколько раз.
 *
 * <p>Существуют 4 возможных варианта расположения патронов после холостого выстрела первого игрока:
 * <ol>
 *   <li>011000 - Вариант 1</li>
 *   <li>001100 - Вариант 2</li>
 *   <li>000110 - Вариант 3</li>
 *   <li>000011 - Вариант 4</li>
 * </ol>
 *
 * <p>Для второго игрока желательные расположения патронов являются 2 и 4, что дает шанс 50 на 50.
 * Однако, если второй игрок решит прокрутить барабан, то количество возможных расположений патронов увеличится до 6:
 * <ol>
 *   <li>011000 - Вариант 1</li>
 *   <li>001100 - Вариант 2</li>
 *   <li>000110 - Вариант 3</li>
 *   <li>000011 - Вариант 4</li>
 *   <li>100001 - Вариант 5</li>
 *   <li>110000 - Вариант 6</li>
 * </ol>
 *
 * <p>Для второго игрока желательные расположения патронов остаются 1 и 3, что дает шанс 0.33.
 */

public class Revolver {
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

        System.out.println("Результаты если сразу нажать на курок");
        calculate(numbersOfExperiments, false);

        System.out.println("Результаты если крутить барабан");
        calculate(numbersOfExperiments, true);
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
     * Выполняет серию экспериментов для определения победителя между двумя игроками.
     * В зависимости от параметра isSpin, определяется, количество возможных вариантов.
     * Если isSpin установлен в true, то количество вариантов равно 4.
     * Если isSpin установлен в false, то - 6.
     * Результаты экспериментов выводятся в консоль, включая количество побед каждого игрока и процент успеха.
     *
     * @param numbersOfExperiments Количество экспериментов, которые следует провести.
     * @param isSpin               Флаг, изменяющий количество вариантов.
     * @throws IllegalArgumentException если numbersOfExperiments меньше или равно нулю.
     */
    private static void calculate(BigDecimal numbersOfExperiments, boolean isSpin) {
        BigDecimal firstPlayerWins = BigDecimal.ZERO;
        BigDecimal secondPlayerWins = BigDecimal.ZERO;
        int bound = 0;

        if (isSpin) {
            bound = 2;
        }

        for (int i = 0; i < numbersOfExperiments.longValue(); i++) {
            int seed = random.nextInt(4 + bound);
            if (seed == 1 || seed == 3) {
                secondPlayerWins = secondPlayerWins.add(BigDecimal.ONE);
            } else {
                firstPlayerWins = firstPlayerWins.add(BigDecimal.ONE);
            }
        }
        BigDecimal percentFirst = calculateWinPercentage(firstPlayerWins, numbersOfExperiments);
        BigDecimal percentSecond = calculateWinPercentage(secondPlayerWins, numbersOfExperiments);

        String firstInfo = "\tПервый победил раз " + firstPlayerWins + ". Процент " + percentFirst;
        String secondInfo = "\tВторой победил раз " + secondPlayerWins + ". Процент " + percentSecond;

        System.out.println(firstInfo);
        System.out.println(secondInfo);
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