package spiellogik;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tobias
 */
public class Karte {
    private String color;
    private String number;
    private int value;
    private boolean trumpf;
    private int order;
    
    Karte(String color, String number, int value, boolean trumpf, int order) {
        this.color=color;
        this.number=number;
        this.value=value;    
        this.trumpf=trumpf;    
        this.order=order;  
    }
    
    
    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }
    /**
     * @return the trumpf
     */
    public boolean getTrumpf() {
        return trumpf;
    }

    /**
     * @param value the trumpf to set
     */
    public void setTrumpf(boolean trumpf) {
        this.trumpf = trumpf;
    }
    
    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @param value the value to set
     */
    public void setOrder(int order) {
        this.order = order;
    }
    
}
