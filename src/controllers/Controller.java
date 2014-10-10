package controllers;

import classes.letter;
import com.sun.istack.internal.NotNull;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class Controller {
    //debug switcher
    private boolean debug = false;

    public MenuItem close_menu;
    public MenuItem clear_menu;
    public MenuItem about_menu;
    public AnchorPane top_anchor;
    public AnchorPane bottom_anchor;
    public TextField task_textField;
    public Button generate_Button;
    public ProgressIndicator processIndicator;

    public void close(ActionEvent actionEvent) {
    }

    public void clear(ActionEvent actionEvent) {
    }

    public void about(ActionEvent actionEvent) {
    }

    public void generate(ActionEvent actionEvent) {
        processIndicator.setVisible(true);
        processIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        long timer=System.currentTimeMillis();

        StringBuffer stringBuffer_task = new StringBuffer(task_textField.getText());

        //debug
        if (debug) System.out.println("stringBuffer_task = " + stringBuffer_task);

        //Розбір введеної строки на слова
        String[] task_word = stringBuffer_task.toString().split("[^0-9a-zA-Zа-яА-Я]+");
        //Розбір введеної строки на спецсимволи (+; -; = .. т.п.)
        String[] task_symbols = stringBuffer_task.toString().split("[0-9a-zA-Zа-яА-Я]+");

        //debug
        if (debug) {
            for (int i = 0; i < task_word.length; i++) {
                System.out.println("task_word" + i + " " + task_word[i]);
            }
            for (int i = 0; i < task_symbols.length; i++) {
                System.out.println("task_symbols" + i + " " + task_symbols[i]);
            }
        }

        ArrayList<letter> letters = new ArrayList<>();
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
                    if (char_word == 0) letters.get(letters.size() - 1).setNum(1);
                }
                if (exist & (char_word == 0)){
                    letters.get(letters.size() - 1).setNum(1);
                }
            }
        }

        //debug
        if (debug) {
            for (int i = 0; i < letters.size(); i++) {
                System.out.print("letters" + i + " " + letters.get(i).getLet() + " = " + letters.get(i).getNum() + "  ");
            }
        }

        processIndicator.setVisible(false);
        System.out.println("time="+(System.currentTimeMillis()-timer));
    }


}
