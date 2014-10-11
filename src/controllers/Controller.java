package controllers;

import classes.letter;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class Controller {
    //debug switcher
    private boolean debug = false;

    /**
     * @param letters      - асоціативний масив
     * @param task_word    - масив слів
     * @param task_symbols - масив символів
     */
    private ArrayList<letter> letters = new ArrayList<>();
    private String[] task_word;
    private String[] task_symbols;

    public MenuItem close_menu;
    public MenuItem clear_menu;
    public MenuItem about_menu;
    public AnchorPane top_anchor;
    public AnchorPane bottom_anchor;
    public TextField task_textField;
    public Button generate_Button;
    public ProgressIndicator processIndicator;

    public void close(ActionEvent actionEvent) {
        System.exit(100);
    }

    public void clear(ActionEvent actionEvent) {
        letters.clear();
        bottom_anchor.getChildren().clear();
    }

    public void about(ActionEvent actionEvent) {
    }

    public void generate(ActionEvent actionEvent) {
        processIndicator.setVisible(true);
        processIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        long timer = System.currentTimeMillis();

        StringBuffer stringBuffer_task = new StringBuffer(task_textField.getText());

        //debug
        if (debug) {
            System.out.println("stringBuffer_task = " + stringBuffer_task);
            //stringBuffer_task = new StringBuffer("A+FAT=ASS");
        }

        //Розбір введеної строки на слова
        task_word = stringBuffer_task.toString().split("[^0-9a-zA-Zа-яА-Я]+");
        //Розбір введеної строки на спецсимволи (+; -; = .. т.п.)
        task_symbols = stringBuffer_task.toString().split("[0-9a-zA-Zа-яА-Я]+");

        //debug
        if (debug) {
            for (int i = 0; i < task_word.length; i++) {
                System.out.println("task_word" + i + " " + task_word[i]);
            }
            for (int i = 0; i < task_symbols.length; i++) {
                System.out.println("task_symbols" + i + " " + task_symbols[i]);
            }
        }


        letter letter_buff = new letter();
        for (int word = 0; word < task_word.length; word++) {
            for (int char_word = 0; char_word < task_word[word].length(); char_word++) {
                boolean exist = false;
                for (int i = 0; i < letters.size(); i++) {
                    if (letters.get(i).getLet() == task_word[word].charAt(char_word)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    letters.add(new letter(task_word[word].charAt(char_word), 0));
                    if (char_word == 0) {
                        letters.get(letters.size() - 1).setNum(1);
                        letters.get(letters.size() - 1).setNot_zero(true);
                    }
                }
                if (exist & (char_word == 0)) {
                    letters.get(letters.size() - 1).setNum(0);
                }
            }
        }
        /*while (letters.get(0).getNum() < 10) {
            //debug
            if (debug) {
                if ((letters.get(0).getNum() == 9) & (letters.get(1).getNum() == 8) & (letters.get(2).getNum() == 0) & (letters.get(3).getNum() == 10)) {
                    System.out.println("lol");
                }
            }
            generator();
        }*/
        MyThread thread = new MyThread();
        thread.start();
        try {
            thread.join();
            //debug
            if (debug) {
                for (int i = 0; i < letters.size(); i++) {
                    System.out.print("letters" + i + " " + letters.get(i).getLet() + " = " + letters.get(i).getNum() + "  ");
                }
            }

            int vpos = 25,
                    hpos = 50;
            ArrayList<Label> labels = new ArrayList<>();
            for (int i = 0; i < letters.get(0).getResnum().size(); i++) {
                for (int j = 0; j < letters.size(); j++) {
                    labels.add(new Label(letters.get(j).getLet() + " = " + letters.get(j).getResnum().get(i)));
                    labels.get(labels.size() - 1).setLayoutX(hpos);
                    labels.get(labels.size() - 1).setLayoutY(vpos);
                    hpos += 45;
                    bottom_anchor.getChildren().add(labels.get(labels.size() - 1));
                }
                vpos += 25;
                hpos = 50;
            }

            //processIndicator.setVisible(false);
            // действия после завершения работы потока
        } catch (InterruptedException x) {}



        System.out.println("time=" + (System.currentTimeMillis() - timer));
    }

    /**
     * @return правильність виконання діїї
     */
    private boolean math_control() {
        int type;
        type = type(task_symbols[1]);

        Integer[] numbers = new Integer[task_word.length];
        for (int i = 0; i < task_word.length; i++) {
            numbers[i] = new Integer(0);
            for (int j = 0; j < task_word[i].length(); j++) {
                double v = num(task_word[i].charAt(j)) * Math.pow(10, (task_word[i].length() - 1 - j));
                numbers[i] = numbers[i] + (int) v;
            }
        }

        //debug
        if (debug) {
            for (int i = 0; i < numbers.length; i++) {
                System.out.print("numb" + i + " " + numbers[i] + "  ");
            }
        }

        if (type == 0) {
            for (int i = 0; i < numbers.length - 1; i++)
                numbers[numbers.length - 1] -= numbers[i];
            if (numbers[numbers.length - 1] == 0) return true;
        } else if (type == 1) {
            for (int i = numbers.length - 1; i > 0; i--)
                numbers[0] -= numbers[i];
            if (numbers[0] == 0) return true;
        } else if (type == 2) {
            double mult = 1;
            for (int i = 0; i < numbers.length - 1; i++)
                mult *= numbers[i];
            if (numbers[numbers.length - 1] == (int) mult) return true;
        } else if (type == 3) {
            double div = 1;
            for (int i = 1; i < numbers.length; i++)
                div *= numbers[i];
            if (numbers[0] == (int) div) return true;
        }
        ;
        return false;
    }

    /**
     * type = 0, symbol +
     * type = 1, symbol -
     * type = 2, symbol *
     * type = 3, symbol /
     * type = 4, symbol =
     * type = 5, action none - error
     *
     * @param symbol - знак математичної дії
     */
    private int type(String symbol) {
        int type;
        if (symbol.equals("+")) type = 0;
        else if (symbol.equals("-")) type = 1;
        else if (symbol.equals("*")) type = 2;
        else if (symbol.equals("/")) type = 3;
        else if (symbol.equals("=")) type = 4;
        else type = 5;
        return type;
    }

    /**
     * @param let - буква для асоціації
     * @return число яке асоціюється з буквою
     */
    private Integer num(char let) {
        for (int i = 0; i < letters.size(); i++) {
            if (letters.get(i).getLet() == let)
                return letters.get(i).getNum();
        }
        return null;
    }

    /**
     * генератор перебору значень асоціативних літер
     */
    private void generator() {
        for (int i = letters.size() - 1; i > 0; i--) {
            if (letters.get(i).getNum() > 9) {
                letters.get(i - 1).numIncrement();
                if (!letters.get(i).isNot_zero())
                    letters.get(i).setNum(0);
                else letters.get(i).setNum(1);
            }
        }
        if (math_control()) {
            boolean repeater = false;
            for (int i = 0; i < letters.size(); i++) {
                if (!repeater)
                    for (int j = 0; j < letters.size(); j++) {
                        if (j != i) if (letters.get(i).getNum() == letters.get(j).getNum()) {
                            repeater = true;
                            break;
                        }
                    }
            }
            if (!repeater) {
                for (int i = 0; i < letters.size(); i++) {
                    letters.get(i).addResnum(letters.get(i).getNum());
                }
            }
        }
        letters.get(letters.size() - 1).numIncrement();

        //debug
        if (debug) {
            for (int i = 0; i < letters.size(); i++) {
                System.out.println("letters" + i + " " + letters.get(i).getLet() + " = " + letters.get(i).getNum() + "  ");
            }
        }
    }
    class MyThread extends Thread {
        public MyThread() {
        }
        public void run() {
            while (letters.get(0).getNum() < 10) {
                //debug
                if (debug) {
                    if ((letters.get(0).getNum() == 9) & (letters.get(1).getNum() == 8) & (letters.get(2).getNum() == 0) & (letters.get(3).getNum() == 10)) {
                        System.out.println("lol");
                    }
                }
                generator();
            }
        }
    }
}
