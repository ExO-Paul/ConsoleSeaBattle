package Sokolchik.Paul.SeaBattle;

import java.util.Scanner;
import java.util.Random;

/**
 * Класс, представляющий игрока (включая компьютерного)
 */
public class Player {

    static Scanner scanner = new Scanner(System.in);
    private boolean isHuman;                            //Является ли игрок человеком
    int enemyDeadShipsCount = 0;                          //Счётчик потопленных кораблей противника
    private boolean nearEnemyShip = false;                //Флаг того, "нащупал" ли AI корабль
    int lastX, lastY, firstX, firstY;                   //Координаты, по которым производился последний выстрел
    Ship.Direction lastDirection;                       //Направление, по которому производился последний выстрел
    String username;                                    //Имя пользователя
    private Field field = new Field();                    //Поле, на котором игрок расставляет свои корабли
    private Field map = new Field();                      //Карта, на которой игрок может посмотреть результаты своих предыдущих выстрелов

    public Field getField() {
        return field;
    }

    public Field getMap() {
        return map;
    }

    /**
     * Конструктор класса Player создаёт экземпляр объекта и запрашивает имя пользователя у игрока - человека
     * (что задаётся передачей соответстующему аргументу значения true)
     */

    public Player(boolean isHuman) {
        this.isHuman = isHuman;

        if (isHuman)
            username = Input.usernameEnter();
    }



    /**
     * Метод описывает процесс расстановки кораблей. Игрок по окончании процесса получает сообщение и видит свои корабли.
     */

    public void createField(boolean isAuto) {
        if (isHuman) {
            this.field = new Field();
            ShipManipulator.createShips(field, isAuto);
            GUI.shipsReady();
            field.setFieldReady();                      //Стираем лишние символы промаха с поля игрока (чтобы было удобнее отображать промахи компьютера)
            GUI.showMap(field);
        } else {
            ShipManipulator.createShips(field, true);
        }
    }

    //REWISE THIS!!!!!!!!!

    /**
     * Метод описывает логику проверки результатов выстрела игрока. Принимает в качестве аргументов координаты выстрела
     * и игрока, по которому производился выстрел. Сначала получается список всех кораблей противника,
     * счётчик уничтоженных кораблей обнуляется. Затем каждый корабль в массиве
     * кораблей противника проверяется на то, произошло ли попадание в него по указанным в аргументах координатам x и y,
     * если попадание имело место, устанавливаются маркеры того, что в клетку карты производился выстрел и того,
     * что в ней находился корабль. Также выводится сообщение о попадании.
     * При этом также идёт подсчёт количества потопленных кораблей и отрисовка вокруг них "рамки" из символов промаха.
     * Далее на экран выводится Карта с соответствующей подписью.
     * Функция также осуществляет контроль того, является ли выстрел победным за счёт сравнения количества
     * потопленных кораблей противника с общим числом краблей на карте. При количестве потопленных кораблей, равном
     * общему количеству, метод возвращает true, иначе - false
     */

    public boolean playerShoot(int x, int y, Player ai) {
        Ship[] ships = ai.getField().getShips();
        int deadCount = 0;
        map.shootCell(x, y);


        for (Ship ship : ships) {
            if (ship.isHit(x, y)) {
                map.getCell(x, y).occupied = true;
                GUI.gotHim();
                if (ship.isDead) {
                    deadCount++;
                    ShipManipulator.setSunk(map, ship);
                }
            }
        }

        System.out.println("\nOur map:");       //Not needed for graphic version!!!
        GUI.showMap(map);

        if (deadCount == SeaBattle.COMMON_COUNT)
            return true;
        else
            return false;
    }

    //REWISE THIS!!!!!!!!!

    /**
     * Метод описывает логику выстрела AI. Принимает в качестве аргумента профиль игрока, по которому производился выстрел.
     * Сначала получается список всех кораблей противника счётчик уничтоженных кораблей обнуляется.
     * Затем если предыдущий выстрел поразил корабль противника (что определяется по маркеру nearEnemyShip),
     * происходит присвоение координатам предыдущих значений, затем установка направления выстрела равным направлению
     * предыдущего выстрела, либо генерируется случайное направление выстрела в зависимости от того, есть ли запись о
     * направлении предыдущего выстрела. Затем, в зависимости от направления, генерируются координаты клетки,
     * куда будет произведён выстрел. В случае, если эти координаты находятся за пределами поля или если по ним уже
     * производился выстрел, происходит установка координат выстрела в место, куда производился первый выстрел, приведший к попаданию,
     * а направление выстрелов переключается на противоположное и опять гененрируются координаты выстрела. В случае,
     * если сгененрированные координаты находятся в пределах поля и по этой ячейке не производился выстрел, генерация координаты
     * выстрела завершается. Иначе повторяются все действия, предусмотренные для случая, когда предыдущийвыстрел поразил
     * корабль противника.
     * В случае, если предыдущий выстрел был промахом или если корабль противника был уничтожен, производится
     * генерация случайных координат в диапазоне [0;10) и, если координаты попали в клетку, куда ранее выстрел
     * уже производился, генерация повторяется снова.
     * Происходит установка маркера произведённого выстрела в клетку в матрице поля игрока.
     * Затем каждый корабль в массиве кораблей противника проверяется на то, произошло ли попадание в него
     * по указанным в аргументах координатам x и y, если попадание имело место, устанавливаются маркеры того,
     * что произошло попадание, координаты и направление выстрела записываются. Если предыдущая запись направления пуста,
     * то координаты записываются также в переменные для координат первого попоадания.
     * При этом также идёт подсчёт количества потопленных кораблей и отрисовка вокруг них "рамки" из символов промаха,
     * Далее на экран выводится Карта с соответствующей подписью.
     * Функция также осуществляет контроль того, является ли выстрел победным за счёт сравнения количества
     * потопленных кораблей противника с общим числом краблей на карте. При количестве потопленных кораблей, равном
     * общему количеству, метод возвращает true, иначе - false
     */

    public boolean aiShoot(Player player) {
        Ship[] ships = player.getField().getShips();
        Random random = new Random();
        int deadCount = 0;
        int x, y;
        Ship.Direction direction = null;

        if (nearEnemyShip) {
            for (; ; ) {
                x = lastX;
                y = lastY;
                if (lastDirection != null)
                    direction = lastDirection;
                else
                    direction = Ship.Direction.values()[random.nextInt(4)];

                switch (direction) {
                    case Right:
                        x += 1;
                        break;
                    case Left:
                        x -= 1;
                        break;
                    case Down:
                        y += 1;
                        break;
                    case Up:
                        y -= 1;
                        break;
                }

                if ((!Field.inRange(x, y) && lastDirection != null) || (Field.inRange(x, y) && lastDirection != null && player.getField().getCell(x, y).wasShot)) {
                    x = firstX;
                    y = firstY;
                    switch (lastDirection) {
                        case Up:
                            direction = Ship.Direction.Down;
                            y += 1;
                            break;
                        case Down:
                            direction = Ship.Direction.Up;
                            y -= 1;
                            break;
                        case Left:
                            direction = Ship.Direction.Right;
                            x += 1;
                            break;
                        case Right:
                            direction = Ship.Direction.Left;
                            x -= 1;
                            break;
                    }
                }


                if (Field.inRange(x, y))
                    if (!player.getField().getCell(x, y).wasShot)
                        break;
            }
        } else

            do {
                x = random.nextInt(10);
                y = random.nextInt(10);

            } while (player.getField().getCell(x, y).wasShot);

        player.getField().shootCell(x, y);

        for (Ship ship : ships) {
            if (ship.isHit(x, y)) {
                nearEnemyShip = true;
                lastX = x;
                lastY = y;

                lastDirection = direction;

                if (lastDirection == null) {
                    firstX = x;
                    firstY = y;
                }

                if (ship.isDead) {
                    ShipManipulator.setSunk(player.getField(), ship);
                    nearEnemyShip = false;
                }
            }
        }

        System.out.println("\nField report:");      //Not needed for graphic version!!!
        GUI.showMap(player.getField());

        if (deadCount == SeaBattle.COMMON_COUNT)
            return true;
        else
            return false;

    }


}
