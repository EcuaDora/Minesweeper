package com.company;

import javax.swing.*;
import java.awt.*;

import static com.company.GameMines.FIELD_SIZE;
import static com.company.GameMines.field;

/**
 * класс наследуемый все от JPanel, отвечает за описание игрового поля
 */
public class Canvas extends JPanel {

    @Override
    /**
     * @param g - объект типа Graphics, отрисовка игрового поля
     */
    public void paint(Graphics g){
        super.paint(g);// вызываем родительский метод отрисовки
        for (int x = 0; x < FIELD_SIZE; x++)// два вложенных цикла - обращаемся к объекту в массиве fields
            for (int y = 0; y < FIELD_SIZE; y ++) field[y][x].paint(g, x, y); //и вызываем метод отрисовки данного объекта
    }
}
