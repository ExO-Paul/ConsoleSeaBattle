package Sokolchik.Paul.SeaBattle;

import java.util.Scanner;

/**
 * Created by ExO on 07.08.2014.
 */
public class SeaBattle {


    static Scanner scanner = new Scanner(System.in);
    static final int FOURDECK_COUNT = 1;
    static final int THREEDECK_COUNT = 2;
    static final int TWODECK_COUNT = 3;
    static final int ONEDECK_COUNT = 4;
    static final int COMMON_COUNT = FOURDECK_COUNT + THREEDECK_COUNT + TWODECK_COUNT + ONEDECK_COUNT;




    public static void main(String[] args) {
        Player player = new Player(true);                                   //Создаём нового игрока-человека
        Player ai = new Player(false);                                      //Создаём игрока - AI
        player.createField(Input.genTypeChoice());
        ai.createField(true);                                                   //Создаём поля, содержащие корабли


        for (; ; )                                                            //Бесконечный цикл стрельбы
        {
            Coordinate coordinate = Input.inputShotCoordinate();
            if (Field.inRange(coordinate.x, coordinate.y) && !player.getMap().getCell(coordinate.x, coordinate.y).wasShot) {      //Если введённые координаты действительно принадлежат полю и на карте игрока нет отметок в этом месте(т.е. туда не велась стрельба)
                if (player.playerShoot(coordinate.x, coordinate.y, ai)) {   //Игрок стреляет, одновременно производится проверка победил он или нет
                    GUI.winner(true, player.username);                          //Если победил - выводим сообщение о победе с указанием имени игрока
                    break;
                }
                if (ai.aiShoot(player)) {                                   //Аналогично с компьютером, но сообщение - о проигрыше с именем игрока
                    GUI.winner(false, player.username);
                    break;
                }
            } else
                GUI.wrongCoordinate();
        }

    }

}




