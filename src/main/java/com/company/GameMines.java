package com.company;

import java.awt.*; // обеспечивает рисование окна
import java.awt.event.*;    // обеспечивает обработку событий
import javax.swing.*;           // чтоб рисовалсь объекты в окне открытом
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;


/**
 *the main class of the game, inherits the properties of the JFrame class. It initializes and graphically displays the field
 */
public class GameMines extends JFrame {

    /**
     * the program name
     */
    public static final String TITLE_OF_PROGRAM = "Сапер";
    /**
     * the flag sign
     */
    public static final String SIGN_OF_FLAG = "f";
    /**
     *the block size in pixels
     */
    public static final int BLOCK_SIZE = 30;
    /**
     *  the field size in blocks
     */
    public static final int FIELD_SIZE = 9;
    /**
     * the right margin
     */
    public static final int FIELD_DX = 16;
    /**
     * the margin at the bottom + additional margin for the timer panel
     */
    public static final int FIELD_DY = 37 + 18;
    /**
     *  coordinates of the window start position (top left corner)
     */
    public static final int START_LOCATION = 200;
    /**
     * return the value when the left mouse button is pressed
     */
    public static final int MOUSE_BUTTON_LEFT = 1;
    /**
     * return the value when the right mouse button is pressed
     */
    public static final int MOUSE_BUTTON_RIGHT = 3;
    /**
     * number of mines
     */
    public static final int NUMBER_OF_MINES = 10;
    /**
     * the colors of the numbers on the cells
     */
    public static final int[] COLOR_OF_NUMBERS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0};

    /**
     * two-dimensional array of cells
     */
    public static Cell[][] field = new Cell[FIELD_SIZE][FIELD_SIZE];

    /**
     * an object that helps randomly place bombs
     */
    public static Random random = new Random();
    /**
     * the variable stores the number of open cells
     */
    public static int countOpenedCells;
    /**
     *Boolean variable, indicates that the user has won
     */
    public static boolean youWon;
    /**
     * Boolean variable, indicates that the mine exploded
     */
    public static boolean bangMine;
    /**
     * explosion coordinates on the OX direction
     */
    public static int bangX;
    /**
     * explosion coordinates on the OY direction
     */
    public static int bangY;
    /**
     * первый клик по полю
     */
    private boolean firstclick;

    /**
     *
     * @param args - main method, creates an instance of the program so that you can see the frame, defines the entry point into the program
     */
    public static void main(String[] args) {

        GameMines main = new GameMines();
        main.setUpWindow();

    }

    /**
     *  main class constructor
     */
     void setUpWindow() {
        firstclick = false;
        setTitle(TITLE_OF_PROGRAM);     // Define the program header
        setDefaultCloseOperation(EXIT_ON_CLOSE); // говорим, что при закрытии окна, прекратится работа программы
        setBounds(START_LOCATION, START_LOCATION, FIELD_SIZE*BLOCK_SIZE+FIELD_DX, FIELD_SIZE*BLOCK_SIZE+FIELD_DY); // устанавливаем стартовую позицию окна и его размеры
        setResizable(false); //устанавливаем, что нельзя менять масштаб окна


        TimerLabel timeLabel = new TimerLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER); // выравниваем по горизонтали


        Canvas canvas = new Canvas();
        canvas.setBackground(Color.white); //устанавливаем цвет фона
        /**
         *определим прослушиватель нажатия на мышь
         */
        canvas.addMouseListener(new MouseAdapter() { //обрабатываем нажатия мыши по полю игры

            @Override       // переопределяем метод mouseReleased()
            public void mouseReleased(MouseEvent e) {

                super.mouseReleased(e); //вызываем метод mouseReleased() у родительского класса

                int x = e.getX()/BLOCK_SIZE; //обе строки обращаемся к е - объект типа MouseEvent
                int y = e.getY()/BLOCK_SIZE; //и получаем ее координаты (координаты клика мыши)

                if (e.getButton() == MOUSE_BUTTON_LEFT && !bangMine && !youWon) // левая кнопка мыши отжата, но я не победил или мина не взорвалась
                    if (field[y][x].isNotOpen()) // если ячейка не открыта, то я ее открываю
                    
                         if (!firstclick) {
                            if (!field[y][x].firstinverse()) {
                                init_one_field_cell(y, x);
                            }
                            firstclick = true;
                        }
                        openCells(x,y);
                        youWon = countOpenedCells == FIELD_SIZE*FIELD_SIZE - NUMBER_OF_MINES; // проверка - может на данный момент я открыл все ячейки и тогда я победил

                        if (bangMine){ //если взорвалась мина флаг-true, то запоминаю ее кординаты
                            bangX = x;
                            bangY = y;
                        }
                    }

                if (e.getButton() == MOUSE_BUTTON_RIGHT) field[y][x].inverseFlag(); // правая кнопка мыши отжата, то инвертирую флаг (ставлю-не ставлю флаг)

                if (bangMine || youWon) timeLabel.stopTimer(); // остановка таймера

                canvas.repaint(); // перерисовываю окно

            }
        });
         /**
          *  канву в центр
          */
         add(BorderLayout.CENTER, canvas);
         /**
          * добавляем таймер вниз
          */
         add(BorderLayout.SOUTH, timeLabel);
        /**
         * делаем окно видимым
         */
        setVisible(true);
        /**
         * инициализация поля
         */
        initField();
    }

    /**
     * рекурсивное  открытие ячеек
     * @param x - координата ячейки по горизонтали( вдоль оси ОХ)
     * @param y - координата ячейки по вертикали(вдоль оси ОУ)
     */
    void openCells(int x, int y) {
        if (x < 0 || x > FIELD_SIZE -1 || y < 0 || y > FIELD_SIZE -1 ) return; // неправильные координаты (за границей поля) - 1 условие выхода из метода
        if (!field[y][x].isNotOpen()) return; // ячейка уже открыта - 2 условие выхода из метода

        field[y][x].open(); //открываем ячейку
        if (field[y][x].getCountBomb() > 0 || bangMine) return; // количество бомб в ячейке больше 0 или бомба взорвалась - 3 условие выхода из метода
        for (int dx = -1; dx <2; dx++) // двойной цикл по соседним 8 ячейкам, который этот метод снова вызывает
            for (int dy = -1; dy <2; dy++) 
                openCells(x + dx, y + dy);
    }

    /**
     * метод инициализации поля
     */
    void initField(){
        int x,y, countMines = 0;

        // создаем ячейки (двумерный массив объектов)
        for (int x1= 0; x1 < FIELD_SIZE; x1++)          //вложенный цикл - создаем каждую клеточку - в каждой клеточке объект Сell()
            for (int y1= 0 ; y1 < FIELD_SIZE; y1 ++)
                field[y1][x1] = new Cell();

        //минируем + мина в ячейке или нет?
        while (countMines < NUMBER_OF_MINES){
            do {
                x = random.nextInt(FIELD_SIZE);
                y = random.nextInt(FIELD_SIZE);
            }
            while (field[y][x].isMined());
            field[y][x].mine();
            countMines ++;
        }
        countMines();
        out();
    }
    private void countMines() {// считаем мины вокруг
        for (x = 0; x < FIELD_SIZE; x++)            // перебор объектов(ячеек) на поле
            for (y = 0; y < FIELD_SIZE; y++)
                if (!field[y][x].isMined()) {
                    int count = 0;
                    for (int dx = -1; dx < 2; dx++)
                        for (int dy = -1; dy < 2; dy++) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (nX < 0 || nY < 0 || nX > FIELD_SIZE -1 || nY > FIELD_SIZE -1) {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nY][nX].isMined()) ? 1:0;
                        }
                    field[y][x].setCountBomb(count);
                }

    }

    void init_one_field_cell(int y0, int x0) {
        int x = random.nextInt(FIELD_SIZE);
        int y = random.nextInt(FIELD_SIZE);
        while (field[y][x].isMined() && (x != x0 && y != y0)) {
            x = random.nextInt(FIELD_SIZE);
            y = random.nextInt(FIELD_SIZE);
        }

        field[y][x].mine();
        countMines();
        System.out.println("Recounted!");
        out();
    }
    
    


