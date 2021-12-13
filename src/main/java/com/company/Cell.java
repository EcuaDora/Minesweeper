package com.company;

import java.awt.*;
import static com.company.GameMines.BLOCK_SIZE;
import static com.company.GameMines.COLOR_OF_NUMBERS;
import static com.company.GameMines.SIGN_OF_FLAG;
import static com.company.GameMines.bangMine;
import static com.company.GameMines.countOpenedCells;
import static com.company.GameMines.youWon;

/**
 * Класс, отвечающий за действия с ячейками и их отрисовку
 */
public class Cell {

    private  boolean isOpen, isMine, isFlag; //isOpen - открыта ячейка или нет, isMine - заминирована ячейка или нет, isFlag - есть флаг или нет
    private int countBombNear; // количество бомб вблизи

    /**
     * открываем ячейку и считаем открытые ячейки
     */
    void open () {
        isOpen = true;
        bangMine = isMine;
        if (!isMine) countOpenedCells++;
    }
    /**
     * минируем ячейку
     */
    void  mine () {
        isMine = true;
    }
    /**
     *
     * @param count - число,  устанавливаем количество бомб рядом (сэттер)
     */
    void setCountBomb(int count) {
        countBombNear = count;
    }
    /**
     *
     * @return возвращает количество бомб (геттер)
     */
    int getCountBomb () {
        return countBombNear;
    }
    /**
     *
     * @return  проверка - открыта или не открыта ячейка
     */
    boolean isNotOpen() {
        return !isOpen;
    }
    /**
     *
     * @return возвращается заминирована ли ячейка или нет
     */
    boolean isMined() {
        return isMine;
    }
    /**
     *  инвертиртирую флаг
     */
    void inverseFlag() {
        isFlag = !isFlag;
    }
    /**
     * рисуем бомбу
     * @param g - объект типа Graphics
     * @param x - координата бомбы по оси ОХ
     * @param y - координата бомбы по оси ОY
     * @param color - цвет бомбы
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
     * рисуем строку, чтоб отображать флаг или цифры в ячейках
     * @param g - объект типа Graphics
     * @param str - строка, которую необходимо перерисовать
     * @param x - координата надписи по оси ОХ
     * @param y - координата надписи по оси ОY
     * @param color - цвет надписи
     */
    void paintString (Graphics g, String str, int x, int y, Color color){
        g.setColor(color);
        g.setFont(new Font("", Font.BOLD, BLOCK_SIZE));
        g.drawString(str, x*BLOCK_SIZE + 8, y*BLOCK_SIZE + 26);
    }

    /**
     * отрисовка игрового поля
     * @param g - объект типа Graphics
     * @param x - координата необходимой отрисовки по оси ОХ
     * @param y - координата необходимой отрисовки по оси ОY
     */ void paint (Graphics g, int x, int y) {
        // расчерчиваем поле на квадраты
        g.setColor(Color.lightGray);
        g.drawRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);

        if (!isOpen) { // если ячейка не открыта, тогда если ... рисуем бомбу, в противном случае рисуем прямоугольник - ячейка закрыта
            if ((bangMine || youWon) &&  isMine) paintBomb(g, x, y, Color.black);
            else {
                g.setColor(Color.lightGray);
                g.fill3DRect(x*BLOCK_SIZE, y*BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, true);
                if (isFlag) paintString(g, SIGN_OF_FLAG, x, y, Color.red); // рисуем флаг
            }
        }
        else if (isMine) paintBomb(g, x, y, bangMine? Color.red : Color.black); //ячейка открыта, то если в ячейке бомба - рисуем бомбу, если бомбы нет, то устанавливаем кол-во бомб в округе
        else if (countBombNear > 0) paintString(g, Integer.toString(countBombNear), x, y, new Color (COLOR_OF_NUMBERS[countBombNear-1]));

    }

}
