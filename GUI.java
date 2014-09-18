package Sokolchik.Paul.SeaBattle;

/**
 * Created by sokolchik_p on 25.08.2014.
 */
public class GUI {

    /**
     * Функция отрисовки поля.
     * Сначала выводит 2 верхние строки, содержащие подписи для горизонтальной оси, затем в каждой следующей строке выводит
     * её номер, начиная с 0, пробел и вертикальную черту для создания разделителя, а после неё - символы в зависимости от числа,
     * содержащегося в очередной ячейке матрицы. В конце строки выводится символ перехода на новую строку.
     */


    static void showMap(Field field) {
        System.out.println("  |0 1 2 3 4 5 6 7 8 9\n--+-------------------");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < 10; j++) {
                if (field.getCell(j, i).occupied) {
                    if (field.getCell(j, i).wasShot)
                        System.out.print("X ");
                    else
                        System.out.print("O ");
                } else if (field.getCell(j, i).wasShot)
                    System.out.print("* ");
                else
                    System.out.print("~ ");
            }
            System.out.println();
        }
    }

    static void winner(boolean win, String username) {
        if (win)
            System.out.print("You won, Admiral " + username + "!");
        else
            System.out.print("Such a misfortune, " + username + " =(");
    }

    static void wrongCoordinate(){
        System.out.println("Would you kindly set valid coordinates, sir?");
    }

    static void gotHim(){
        System.out.println("You got him!");
    }

    static void shipsReady(){
        System.out.println("Your ships are ready");
    }

    static void wrongPosition(){
        System.out.println("Please, repeat enter");
    }

    static void shipSunk(){
        System.out.println("The ship is sunk");
    }

    /*public static Coordinate coordinateChoice(){
        return new Coordinate(x,y);

    }*/

   /* public static Ship.Direction directionChoice(){

        return
    }*/

}
