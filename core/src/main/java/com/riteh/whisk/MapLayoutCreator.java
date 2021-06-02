package com.riteh.whisk;

import com.badlogic.gdx.utils.Array;

public class MapLayoutCreator {
    private int MAX_X = 10; //max x dimension for layout matrix
    private int MAX_Y = 10; //max y dimension for layout matrix
    public int[][] layout; //2D array that stores the layout
    public String[][] mapConstructorList; //array of constructors-> number of maps * number of constructor arguments
    public static final int MAX_MAPS = 10; //maximum number of maps
    public static int CURRENT_MAP;

    public MapLayoutCreator(/*add needed constructor params*/) {
        //add needed constructor stuff
    }

    //function that generates layout (RNG code goes here), and calls the method for creating constructors
    public void layoutRNG(/*add needed params*/) {
        generateConstructors();
    }

    //function that generates mapConstructorList aka array of constructors based on the 2D layout matrix
    public void generateConstructors() {
        this.mapConstructorList = new String[9][5];
        //fill mapConstructorList
    }

 }
