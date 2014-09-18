package Sokolchik.Paul.SeaBattle;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by sokolchik_p on 01.09.2014.
 */
public class ShipManipulator {
    static boolean isAutoGenerated = false;


    /**
     * Функция создания кораблей.
     * В функции изначально создается массив объектов класса Ship, содержащий количество элементов,
     * соответствующее общему числу кораблей, заданному в основном классе программы.
     * Затем проводится заполнение массива экземплярами класса ship с параметрами strength = 4 (4-палубный корабль), 3, 2 и 1
     * в количествах, заданных в основном классе программы. при этом конструкторам этих объектов передаются значения параметра isAutoGenerated
     * и ссылка на экзепляр класса Поле с помощью ключевого слова this.
     *
     * @param isAutoGenerated - логическое значение, определяющее, будут ли корабли генерироваться автоматически или по результатм запроса
     */

    public static void createShips(Field field, boolean isAutoGenerated) {
        Ship[] ships = field.getShips();
        for (int i = 0; i < SeaBattle.COMMON_COUNT; i++)
            if (i < SeaBattle.FOURDECK_COUNT)
                ships[i] = shipGenerate(4, isAutoGenerated, field);
            else if (i < SeaBattle.FOURDECK_COUNT + SeaBattle.THREEDECK_COUNT)
                ships[i] = shipGenerate(3, isAutoGenerated, field);
            else if (i < SeaBattle.FOURDECK_COUNT + SeaBattle.THREEDECK_COUNT + SeaBattle.TWODECK_COUNT)
                ships[i] = shipGenerate(2, isAutoGenerated, field);
            else if (i < SeaBattle.COMMON_COUNT - 1)
                ships[i] = shipGenerate(1, isAutoGenerated, field);
            else
                ships[i] = shipGenerate(1, isAutoGenerated, field);
    }


    public static Ship shipGenerate(int strength, boolean isAuto, Field field) {

        if (isAuto)
            isAutoGenerated = isAuto;

        if (isAutoGenerated)
            return autoGenerate(strength, field);
        else
            return shipsPlace(strength, field);
    }


    /**
     * Функция проверки того, лежит ли корабль на свободных клетках или пересекает клетки другого корабля или запретной зоны
     * В начале создаётся и устанавливается в значение true флаг прохождения кораблём проверки. Затем выполняется проверка на то, входят
     * ли координаты корабля в поле. В случае, если нет - флаг прохождения кораблём проверки устанавливается в false.
     * Если да - то для координат x от минимальной до максимальной и для координат y от минимальной до максимальной
     * (т.е. для всех координат точек корабля) выполняется проверка свободности поля в этих координатах. Если поле
     * свободно и предыдущее значение флага прохождения кораблём проверки - true, значение флага оставляется равным true, иначе
     * флаг устанавливает в значение false. По результатам выполнения возвращается значение флага прохождения кораблём проверки
     * clear.
     *
     * @param ship - корабль, который необходимо проверить
     * @return Возвращает булево значение. В случае, если координаты клетки находятся свободной зоне,
     * возвращаетcя true, в обратном случае - false
     */

    public static boolean clearCheck(Field field, Ship ship) {
        boolean clear = true;
        if (!Field.inRange(ship.end.x, ship.end.y) || !Field.inRange(ship.beginning.x, ship.beginning.y))
            clear = false;
        else {
            for (int i = Math.min(ship.beginning.y, ship.end.y); i <= (Math.min(ship.beginning.y, ship.end.y) + Math.abs(ship.beginning.y - ship.end.y)); i++)
                for (int j = Math.min(ship.beginning.x, ship.end.x); j <= (Math.min(ship.beginning.x, ship.end.x) + Math.abs(ship.beginning.x - ship.end.x)); j++) {
                    if (!field.getCell(i, j).occupied && !field.getCell(i, j).wasShot && clear)
                        clear = true;
                    else
                        clear = false;
                }
        }
        return clear;
    }


    /**
     * Функция занесения на поле сегмента корабля вместе с запретной зоной вокруг него.
     * Для координат x от минимальной до максимальной и для координат y от минимальной до максимальной
     * (т.е. для всех координат точек корабля) выполняется установка значения ячеек матрицы поля в значение 2, а затем,
     * для всех координат, находящихся в радиусе 1 клетки от данной, выполняется проверка на принадлежность этих клеток
     * к игровому полю, и, если на них не располагается кораблей, производится заполнение клеток с этими координатами, единицами.
     *
     * @param ship - принимает экземпляр корабля, для которого необходимо выполнить занесение.
     */

    static void setShip(Field field, Ship ship) {
        for (int i = Math.min(ship.beginning.y, ship.end.y); i <= Math.max(ship.beginning.y, ship.end.y); i++)
            for (int j = Math.min(ship.beginning.x, ship.end.x); j <= Math.max(ship.beginning.x, ship.end.x); j++) {
                field.getCell(i, j).occupied = true;
                field.getCell(i, j).wasShot = false;
                for (int k = -1; k <= 1; k++)
                    for (int l = -1; l <= 1; l++) {
                        if (Field.inRange(i + k, j + l))
                            if (!field.getCell(i + k, j + l).occupied)
                                field.getCell(i + k, j + l).wasShot = true;

                    }
            }
    }

    /**
     * Функция занесения на поле символов промаха вокруг потопленного корабля.
     * Для координат x от минимальной до максимальной и для координат y от минимальной до максимальной
     * (т.е. для всех координат точек корабля) выполняется проверка всех координат, находящихся в радиусе 1 клетки
     * от них на принадлежность этих клеток к игровому полю, и, если на них не располагается кораблей,
     * производится заполнение клеток с этими координатами, единицами.
     *
     * @param ship - принимает экземпляр корабля, для которого необходимо выполнить установку символов промаха.
     */

    static void setSunk(Field field, Ship ship) {
        for (int i = Math.min(ship.beginning.y, ship.end.y); i <= Math.max(ship.beginning.y, ship.end.y); i++) {
            for (int j = Math.min(ship.beginning.x, ship.end.x); j <= Math.max(ship.beginning.x, ship.end.x); j++)
                for (int k = -1; k <= 1; k++)
                    for (int l = -1; l <= 1; l++) {
                        if (Field.inRange(i + k, j + l))
                            field.getCell(i + k, j + l).wasShot = true;
                    }
        }
    }


    /**
     * Функция автоматической генерации кораблей.
     * Устанавливает живучесть корабля в соответствии с заданной в параметре. Затем осуществляет генерацию
     * случайных координат для носа корабля в диапазоне [0;10) и направления корабля (от носа к корме). Затем,
     * в зависимости от направления, определяет координаты кормы корабля, после чего проводит проверку сгенерированнойго корабля
     * на то, стоит ли он на свободном пространстве или находится в уже занятом. Если корабль пересекает занятую клетку или стоит
     * в "запрещённой" клетке, число, характеризующее направление, сбрасывается в 0, после чего цикл генерации повторяется снова.
     * Если же корабль проходит проверку, выполняется вызов функции отрисовки корабля на поле с "запрещёнными" клетками вокруг него.
     *
     * @param strength - определяет живучесть корабля
     * @param field    - экземпляр класса "Поле", на котором будут располагаться корабли.
     */

    private static Ship autoGenerate(int strength, Field field) {
        Ship ship = new Ship();
        ship.strength = strength;
        Random rand = new Random();
        Ship.Direction direction;
        boolean directionSet;


        do {
            ship.beginning.x = rand.nextInt(10);
            ship.beginning.y = rand.nextInt(10);
            direction = Ship.Direction.values()[rand.nextInt(4)];
            directionSet = true;

            switch (direction) {
                case Up:
                    ship.end.x = ship.beginning.x;
                    ship.end.y = ship.beginning.y - (strength - 1);
                    if (!clearCheck(field, ship)) {
                        directionSet = false;
                    } else {
                        setShip(field, ship);
                    }
                    break;
                case Right:
                    ship.end.x = ship.beginning.x + (strength - 1);
                    ship.end.y = ship.beginning.y;
                    if (!clearCheck(field, ship)) {
                        directionSet = false;
                    } else {
                        setShip(field, ship);
                    }
                    break;
                case Down:
                    ship.end.x = ship.beginning.x;
                    ship.end.y = ship.beginning.y + (strength - 1);
                    if (!clearCheck(field, ship)) {
                        directionSet = false;
                    } else {
                        setShip(field, ship);
                    }
                    break;
                case Left:
                    ship.end.x = ship.beginning.x - (strength - 1);
                    ship.end.y = ship.beginning.y;
                    if (!clearCheck(field, ship)) {
                        directionSet = false;
                    } else {
                        setShip(field, ship);
                    }
                    break;
                default:
            }
        } while (!directionSet);
        return ship;
    }

    /**
     * Функция ручной расстановки кораблей.
     * Устанавливает живучесть корабля в соответствии с заданной в параметре.
     * Затем осуществляет запрос на ввод пользователем координат носа корабля и направляения его кормы относительно носа,
     * осуществляется проверка правильности ввода цифровых данных. После этого, в зависимости от направления,
     * определяются координаты кормы корабля, после чего проводится проверка корабля на то, стоит ли он на
     * свободном пространстве или находится в уже занятом. Если корабль пересекает занятую клетку или стоит
     * в "запрещённой" клетке, число, характеризующее направление, сбрасывается в 0, выполняется запрос повторного ввода,
     * после чего цикл генерации повторяется снова. Если же корабль проходит проверку, выполняется вызов функции отрисовки
     * корабля на поле с "запрещёнными" клетками вокруг него. После этого метод выводит поле игрока для визуального контроля
     * за расположением корабля.
     *
     * @param strength - определяет живучесть корабля
     * @param field    - экземпляр класса "Поле", на котором будут располагаться корабли.
     */

    private static Ship shipsPlace (int strength, Field field) {
        Ship ship = new Ship();
        ship.strength = strength;
        boolean shipSet;
        Scanner scanner = new Scanner(System.in);
        do {
            shipSet = true;
            Coordinate nose = Input.inputNoseCoordinate(strength);
            ship.beginning.x = nose.x;
            ship.beginning.y = nose.y;

            switch (Input.inputShipDirection()) {
                case Up:
                    ship.end.x = ship.beginning.x;
                    ship.end.y = ship.beginning.y - (strength - 1);
                    if (!clearCheck(field, ship)) {
                        shipSet = false;
                        GUI.wrongPosition();
                    } else {
                        setShip(field, ship);
                    }
                    break;
                case Right:
                    ship.end.x = ship.beginning.x + (strength - 1);
                    ship.end.y = ship.beginning.y;
                    if (!clearCheck(field, ship)) {
                        shipSet = false;
                        GUI.wrongPosition();
                    } else {
                        setShip(field, ship);
                    }
                    break;
                case Down:
                    ship.end.x = ship.beginning.x;
                    ship.end.y = ship.beginning.y + (strength - 1);
                    if (!clearCheck(field, ship)) {
                        shipSet = false;
                        GUI.wrongPosition();
                    } else {
                        setShip(field, ship);
                    }
                    break;
                case Left:
                    ship.end.x = ship.beginning.x - (strength - 1);
                    ship.end.y = ship.beginning.y;
                    if (!clearCheck(field, ship)) {
                        shipSet = false;
                        GUI.wrongPosition();
                    } else {
                        setShip(field, ship);
                    }
                    break;

            }

        } while (!shipSet);

        GUI.showMap(field);
        return ship;
    }


}