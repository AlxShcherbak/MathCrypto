package classes;

import com.sun.deploy.security.MozillaJSSDSASignature;

import java.util.ArrayList;

/**
 * Created by Alx Shcherbak on 10.10.2014.
 */

/**
 * Клас описуючий зв'язок букви з числом
 * let - буква
 * num - число від 0 до 9
 * resnum - масив правильних (результуючих) значень num.
 */
public class letter {
    private char let;
    private Integer num;
    private ArrayList<Integer> resnum;

    public letter(){
        this.resnum = new ArrayList<Integer>();
        this.num = new Integer(0);
        this.let = ' ';
    }

    /**
     * @param let - буква
     * @param num - число від 0 до 9
     */
    public letter(char let,Integer num){
        this.let = let;
        this.num = num;
    }

    public letter(Integer num){
        this.num = num;
    }

    public char getLet() {
        return let;
    }

    public void setLet(char let) {
        this.let = let;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public ArrayList<Integer> getResnum() {
        return resnum;
    }

    public void setResnum(ArrayList<Integer> resnum) {
        this.resnum = resnum;
    }
}
