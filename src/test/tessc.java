package test;

import classes.letter;
import run.funk;

import java.util.ArrayList;

/**
 * Created by Alx Shcherbak on 19.02.2015.
 */
public class tessc {
    public static void main(String[] args) {
        funk funk = new run.funk();
        ArrayList<letter> res = funk.DoIt("один+один=много");
        if (res.size() > 0) {
            for (int ii = 0; ii < res.get(0).getResnum().size(); ii++) {
                for (int i = 0; i < res.size() - 1; i++) {
                    System.out.print(res.get(i).getResnum().get(ii) + "|");
                }
                System.out.println();
            }
        }
    }
}
