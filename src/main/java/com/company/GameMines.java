package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;


/**
 * the main class of the game, inherits the properties of the JFrame class. It initializes and graphically displays the field
 */
class GameMines extends JFrame
{

    /**
     * the program name
     */
    public static final String TITLE_OF_PROGRAM = "Сапер";
    /**
     * the flag sign
     */
    public static final String SIGN_OF_FLAG = "f";
    /**
     * the block size in pixels
     */
    public static final int BLOCK_SIZE = 30;
    /**
     * the field size in blocks
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
     * coordinates of the window start position (top left corner)
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
     * Boolean variable, indicates that the user has won
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
     * !!!!!!!!!! первый клик
     */
    private boolean firstclick;

    /**
     * @param args - main method, creates an instance of the program so that you can see the frame, defines the entry point into the program
     */
    public static void main(String[] args)
    {

        GameMines main = new GameMines();
        main.setUpWindow();

    }

    /**
     * main class constructor
     */
    void setUpWindow()
    {
        firstclick = false;
        setTitle(TITLE_OF_PROGRAM);     // Define the program header
        setDefaultCloseOperation(EXIT_ON_CLOSE); // the program stops working when the window is closed
        setBounds(START_LOCATION, START_LOCATION, FIELD_SIZE * BLOCK_SIZE + FIELD_DX, FIELD_SIZE * BLOCK_SIZE + FIELD_DY); // set the starting position of the window and its size
        setResizable(false); // set that the window cannot be changed to a different scale


        TimerLabel timeLabel = new TimerLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER); // align horizontally


        Canvas canvas = new Canvas();
        canvas.setBackground(Color.white); //set the background color
        /**
         * define a mouse click listener
         */
        canvas.addMouseListener(new MouseAdapter()
        {


            @Override
            public void mouseReleased(MouseEvent e)
            {

                super.mouseReleased(e); // call the mouseReleased() method of the parent class

                int x = e.getX() / BLOCK_SIZE; // both lines access the e - object of MouseEvent type and
                int y = e.getY() / BLOCK_SIZE; // get its coordinates (mouse click coordinates)

                if (e.getButton() == MOUSE_BUTTON_LEFT && !bangMine && !youWon) // the left mouse button is pressed, but the user has not won or the mine has not exploded
                    if (field[y][x].isNotOpen()) // if a cell is not open, I open it
                    {
                        if (!firstclick)
                        {
                            if (!field[y][x].firstinverse())
                            {
                                init_one_field_cell(y, x);
                            }
                            firstclick = true;
                        }

                        openCells(x, y);
                        youWon = countOpenedCells == FIELD_SIZE * FIELD_SIZE - NUMBER_OF_MINES; // check - maybe at this point I have opened all the cells and then I have won

                        if (bangMine)
                        { // if a flag-true mine explodes, I remember its coordinates
                            bangX = x;
                            bangY = y;
                        }
                    }

                if (e.getButton() == MOUSE_BUTTON_RIGHT)
                {
                    field[y][x].inverseFlag();
                } // the right mouse button is depressed, then I invert the flag (put or not put the flag)

                if (bangMine || youWon) timeLabel.stopTimer(); // timer stop

                canvas.repaint(); // redrawing the window

            }
        });
        /**
         *  put the canvass in the center
         */
        add(BorderLayout.CENTER, canvas);
        /**
         * add a timer down
         */
        add(BorderLayout.SOUTH, timeLabel);
        /**
         * make the window visible
         */
        setVisible(true);
        /**
         * field initialization
         */
        initField();
    }

    /**
     * recursive cell opening
     *
     * @param x - coordinate of the cell horizontally (along the OX axis)
     * @param y - vertical coordinate of the cell (along the OY axis)
     */
    void openCells(int x, int y)
    {
        if (x < 0 || x > FIELD_SIZE - 1 || y < 0 || y > FIELD_SIZE - 1)
            return; // incorrect coordinates (outside the field boundary) - 1 condition for exiting the method
        if (!field[y][x].isNotOpen()) return; // cell is already open - 2 the exit condition of the method

        field[y][x].open(); //open the cell
        if (field[y][x].getCountBomb() > 0 || bangMine)
            return; // the number of bombs in the cell is greater than 0 or the bomb has exploded - 3 condition for exiting the method
        for (int dx = -1; dx < 2; dx++) // double loop through the neighboring 8 cells, which this method calls again
            for (int dy = -1; dy < 2; dy++) openCells(x + dx, y + dy);
    }

    /**
     * field initialization method
     */
    void initField()
    {
        int x, y, countMines = 0;

        // create cells (two-dimensional array of objects)
        for (int x1 = 0; x1 < FIELD_SIZE; x1++)          // create cells (two-dimensional array of objects)
            for (int y1 = 0; y1 < FIELD_SIZE; y1++)
                field[y1][x1] = new Cell();

        //mined and check whether there is a mine in the cell or not?
        while (countMines < NUMBER_OF_MINES)
        {
            do
            {
                x = random.nextInt(FIELD_SIZE);
                y = random.nextInt(FIELD_SIZE);
            }
            while (field[y][x].isMined());
            field[y][x].mine();
            countMines++;
        }

        countMines();
        out();
    }
    private void countMines()
    {
        // count the mines around
        for (int x = 0; x < FIELD_SIZE; x++)            // to enumerate objects (cells) on the field
            for (int y = 0; y < FIELD_SIZE; y++)
                if (!field[y][x].isMined())
                {
                    int count = 0;
                    for (int dx = -1; dx < 2; dx++)
                        for (int dy = -1; dy < 2; dy++)
                        {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (nX < 0 || nY < 0 || nX > FIELD_SIZE - 1 || nY > FIELD_SIZE - 1)
                            {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nY][nX].isMined()) ? 1 : 0;
                        }
                    field[y][x].setCountBomb(count);
                }
    }

    void init_one_field_cell(int y0, int x0)
    {
        int x = random.nextInt(FIELD_SIZE);
        int y = random.nextInt(FIELD_SIZE);
        while (field[y][x].isMined() && (x != x0 && y != y0))
        {
            x = random.nextInt(FIELD_SIZE);
            y = random.nextInt(FIELD_SIZE);
        }

        field[y][x].mine();
        countMines();
        System.out.println("Recounted!");
        out();
    }
    private void out()
    {
        for(int i = 0; i < FIELD_SIZE; i++)
        {
            for(int j = 0; j < FIELD_SIZE; j++)
            {
                if(field[i][j].isMined())
                    System.out.print(1);
                else
                    System.out.print(0);
            }
            System.out.println();
        }
    }
}



