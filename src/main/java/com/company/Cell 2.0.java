package com.company;

import java.awt.*;
import static com.company.GameMines.BLOCK_SIZE;
import static com.company.GameMines.COLOR_OF_NUMBERS;
import static com.company.GameMines.SIGN_OF_FLAG;
import static com.company.GameMines.bangMine;
import static com.company.GameMines.countOpenedCells;
import static com.company.GameMines.youWon;

/**
 * The class responsible for actions with cells and their drawing
 */
public class Cell {

    private  boolean isOpen, isMine, isFlag; // isOpen - is the cell open or not, isMine - is the cell mined or not, isFlag - is there a flag or not
    private int countBombNear; // number of bombs near

    /**
     * open the cell and count the open cells
     */
    void open () {
        isOpen = true;
        bangMine = isMine;
        if (!isMine) countOpenedCells++;
    }
    /**
     * put a mine in the cell
     */
    void  mine () {
        isMine = true;
    }
    /**
     *
     * @param count - number, set the number of bombs near (setter)
     */
    void setCountBomb(int count) {
        countBombNear = count;
    }
    /**
     *
     * @return returns the number of bombs (getter)
     */
    int getCountBomb () {
        return countBombNear;
    }
    /**
     *
     * @return  check whether the cell is open or not
     */
    boolean isNotOpen() {
        return !isOpen;
    }
    /**
     *
     * @return returns whether the cell is booby-trapped or not
     */
    boolean isMined() {
        return isMine;
    }
    /**
     *  inverting the flag
     */
    void inverseFlag() {
        isFlag = !isFlag;
    }
    boolean firstinverse()
    {
        if (this.isMined())
        { isMine = false;
            return false;
        }
        else
        {
            return true;
        }
    }
    /**
     * Drawing the bomb
     * @param g - an object of type Graphics
     * @param x - coordinate of the bomb along the OX axis
     * @param y - coordinate of the bomb along the OY-axis
     * @param color - bomb color
     */
    void paintBomb (Graphics g, int x, int y, Color color){
        g.setColor(color);
        g.fillRect(x*BLOCK_SIZE + 7, y*BLOCK_SIZE + 10, 18, 10);
        g.fillRect(x*BLOCK_SIZE + 11, y*BLOCK_SIZE + 6, 10, 18);
        g.fillRect(x*BLOCK_SIZE + 9, y*BLOCK_SIZE + 8, 14, 14);
        g.setColor(Color.white);
        g.fillRect(x*BLOCK_SIZE + 11, y*BLOCK_SIZE + 10, 4, 4);
    }
    /**
     * draw a line to display a flag or numbers in the cells
     * @param g - an object of type Graphics
     * @param str - the line that needs to be redrawn
     * @param x - coordinate of the inscription along the ОХ axis
     * @param y - coordinate of the inscription along the OY axis
     * @param color - inscription color
     */
    void paintString (Graphics g, String str, int x, int y, Color color){
        g.setColor(color);
        g.setFont(new Font("", Font.BOLD, BLOCK_SIZE));
        g.drawString(str, x*BLOCK_SIZE + 8, y*BLOCK_SIZE + 26);
    }

    /**
     * Drawing the playing field
     * @param g - an object of type Graphics
     * @param x - coordinate of the required drawing along the OX axis
     * @param y - coordinate of the required drawing along the ОY axis
     */ void paint (Graphics g, int x, int y) {
        // расчерчиваем поле на квадраты
        g.setColor(Color.lightGray);
        g.drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

        if (!isOpen) { // if the cell is not open, then if ... draw a bomb, otherwise draw a rectangle - the cell is closed
            if ((bangMine || youWon) &&  isMine) paintBomb(g, x, y, Color.black);
            else {
                g.setColor(Color.lightGray);
                g.fill3DRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, true);
                if (isFlag) paintString(g, SIGN_OF_FLAG, x, y, Color.red); // Drawing the flag
            }
        }
        else if (isMine) paintBomb(g, x, y, bangMine? Color.red : Color.black); // cell is open, then if there is a bomb in the cell - draw a bomb, if there is no bomb, then set the number of bombs in the area
        else if (countBombNear > 0) paintString(g, Integer.toString(countBombNear), x, y, new Color (COLOR_OF_NUMBERS[countBombNear-1]));

    }

}
