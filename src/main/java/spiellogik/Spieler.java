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



public class Spieler {
    private int punktestand=0;
    public int nummer;
    public String username;
    public String cId;
    


	public Spieler(String cId,String username) {
         this.username = username;
         this.cId = cId;
        }


        /**
         * @return the punktestand
         */
        public int getpunktestand() {
            return punktestand;
        }

        /**
         * @param punktestand the color to set
         */
        public void setpunktestand(int punktestand) {
            this.punktestand = punktestand;
    }
        
        
        public String getUsername() {
    		return username;
    	}


    	public void setUsername(String username) {
    		this.username = username;
    	}


    	public String getcId() {
    		return cId;
    	}


    	public void setcId(String cId) {
    		this.cId = cId;
    	}

}