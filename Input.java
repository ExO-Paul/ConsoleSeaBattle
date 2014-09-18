package Sokolchik.Paul.SeaBattle;

import java.util.Scanner;

/**
 * Created by sokolchik_p on 02.09.2014.
 */
public class Input {

    static Scanner scanner = new Scanner(System.in);

    static boolean genTypeChoice() {
        int choice = 0;
        boolean isAuto;
        do {
            System.out.println("Who do you want to want to place your ships?\n1-Your aide;\n2-You with your own hands");
            try {
                choice = scanner.nextInt();
            } catch (Exception ex) {
                System.out.println("Sorry, sir, I haven't heard you well");
                scanner = new Scanner(System.in);
            }
        } while (choice != 1 && choice != 2);


        if (choice == 1)
            isAuto = true;
        else if (choice == 2)
            isAuto = false;
        else
            isAuto = false;
        return isAuto;
    }

    static Coordinate inputNoseCoordinate(int strength){
        Coordinate coordinate = new Coordinate();
        try {
            System.out.println("Please,enter X coordinate of the nose of your " + strength + "-deck ship: ");
            coordinate.x = scanner.nextInt();
            System.out.println("Please,enter Y coordinate of the nose of your " + strength + "-deck ship: ");
            coordinate.y = scanner.nextInt();
        } catch (Exception ex) {
            scanner = new Scanner(System.in);
        }
        return coordinate;
    }

    static Ship.Direction inputShipDirection(){
        Ship.Direction direction = Ship.Direction.Undefined;
        do {
            try {
                System.out.println("Please, define the direction of the ship:\nUp, Down, Left or Right");
                direction = Ship.Direction.valueOf(scanner.next());
            } catch (Exception ex) {
                    /*directionSet = false;*/
                scanner = new Scanner(System.in);
            }
        } while (direction== Ship.Direction.Undefined);

        return direction;
    }

    static Coordinate inputShotCoordinate() {
        Coordinate coordinate = new Coordinate();
        try {
            System.out.print("\nEnter X coordinate of your shot: ");
            coordinate.x = scanner.nextInt();
            System.out.print("\nEnter Y coordinate of your shot: ");
            coordinate.y = scanner.nextInt();
        } catch (java.util.InputMismatchException ex) {                   //Обработка исключения ввода
            coordinate.x = 10;
            coordinate.y = 10;
            scanner = new Scanner(System.in);
        }
        return coordinate;
    }

    static String usernameEnter() {
        System.out.print("Please, enter your name: ");
        String username = scanner.nextLine();
        System.out.print("\nWelcome, " + username + "! We're glad you're with us here\n\n");
        return username;
    }
}
