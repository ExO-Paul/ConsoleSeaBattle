package Sokolchik.Paul.SeaBattle;

import java.util.Random;
import java.util.Scanner;

/**
 * Класс, представляющий отдельно взятый корабль
 */

public class Ship {

    public Coordinate beginning = new Coordinate();     //Координаты "начала" (носа) корабля
    public Coordinate end = new Coordinate();           //Координаты "конца" (кормы) корабля
    int strength;                                       //Сила (живучесть) корабля
    public boolean isDead = false;                      //Флаг потопления корабля

    enum Direction {Up, Right, Down, Left, Undefined}

    ;

    /**
     * Функция проверки попадания в корабль.
     * В начале функции генерируется маркер попадания с начальным значением false. Затем, для координат x от минимальной до
     * максимальной и для координат y от минимальной до максимальной (т.е. для всех координат точек корабля),
     * выполняется проверка совпадения этих координат с координатами выстрела. При их совпадении, живучесть корабля снижается
     * на единицу, маркер попадания устанавливается в true, выполняется проверка на то, остались ли у корабля в запасе
     * очки живучести. Если число очков живучести равно 0, то корабль объявляется потопленным установкой флага потопления корабля
     * и выводом соответствующего сообщения. После выполнения всех проверок выполняется возврат в вызывающую функцию значения
     * маркера попадания.
     *
     * @param x - координата x выстрела
     * @param y - координата y выстрела
     * @return Возвращает булево значение, характеризующее то, произошло попадания еили нет
     */

    public boolean isHit(int x, int y) {
        boolean hitMarker = false;

        for (int i = Math.min(beginning.x, end.x); i <= Math.max(beginning.x, end.x); i++)
            for (int j = Math.min(beginning.y, end.y); j <= Math.max(beginning.y, end.y); j++) {
                if (j == x && i == y) {
                    strength -= 1;
                    hitMarker = true;
                    if (strength == 0) {
                        isDead = true;
                        GUI.shipSunk();
                    }

                }
            }

        return hitMarker;
    }

}