package com.riteh.whisk;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;



public class MapLayoutCreator {
    private int MAX_X = 15; //max x dimension for layout matrix
    private int MAX_Y = 15; //max y dimension for layout matrix
    private int SX = 7;
    private int SY = 7;
    public char[][] layout; //2D array that stores the layout
    public static final int MAX_MAPS = 10; //maximum number of maps
    public static int CURRENT_MAP;
    boolean check;
    boolean hasExit;

    public ArrayList<mapClass> mapConstructorList;


    public MapLayoutCreator() {
        this.layout = new char[15][15];
        this.layoutRNG();
    }

    //function that generates layout (RNG code goes here), and calls the method for creating constructors
    public void layoutRNG() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        check = false;

        while (!check) {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    this.layout[i][j] = '-';
                }
            }
            this.layout[this.SX][this.SY] = 'S';
            hasExit = false;
            this.seed(this.SX, this.SY, 6, random);

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j <15; j++) {
                    if (this.layout[i][j] == 'E') check = true;
                }
            }
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j <15; j++) {
                if (this.layout[i][j] == '/') this.layout[i][j] = '-';
                if (this.layout[i][j] == '1') {
                    Integer stage = ThreadLocalRandom.current().nextInt(0, 10);
                    this.layout[i][j] = stage.toString().charAt(0);
                }
            }
        }
        this.generateConstructors();
    }

    public void seed(int i, int j, int difficulty, ThreadLocalRandom random) {
        int chance;

        if (difficulty == 0) {
            if (!hasExit) {
                this.layout[i][j] = 'E';
                hasExit = true;
            } else {
                this.layout[i][j] = '1';
                return;
            }
        }

        if (this.layout[i+1][j] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (i+1) < 10) {
                this.layout[i+1][j] = '1';
                seed(i+1, j, difficulty-1, random);
            } else this.layout[i+1][j] = '/';
        }

        if (this.layout[i-1][j] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (i-1) > 0) {
                this.layout[i-1][j] = '1';
                seed(i-1, j, difficulty-1, random);
            } else this.layout[i-1][j] = '/';
        }

        if (this.layout[i][j+1] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (j+1) < 10) {
                this.layout[i][j+1] = '1';
                seed(i, j+1, difficulty-1, random);
            } else this.layout[i][j+1] = '/';
        }

        if (this.layout[i][j-1] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (j-1) > 0) {
                this.layout[i][j-1] = '1';
                seed(i, j-1, difficulty-1, random);
            } else this.layout[i][j-1] = '/';
        }
    }

    //function that generates mapConstructorList aka array of constructors based on the 2D layout matrix
    public void generateConstructors() {
        mapClass map;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (this.layout[i][j] != '-') {
                    boolean north = false;
                    boolean south = false;
                    boolean west = false;
                    boolean east = false;
                    String type;

                    if (i>0) if (this.layout[i-1][j] != '-') north = true;
                    if (i<14) if (this.layout[i+1][j] != '-') south = true;
                    if (j>0) if (this.layout[i][j-1] != '-') west = true;
                    if (j<14) if (this.layout[i][j+1] != '-') east = true;

                    if (this.layout[i][j] == 'S') {
                        type = "start";
                        this.layout[i][j] = '1';
                    } else if (this.layout[i][j] == 'E') {
                        type = "end";
                        this.layout[i][j] = '1';
                    }
                    else type = "room";

                    StringBuilder sb = new StringBuilder("Maps/level");
                    sb.append(this.layout[i][j]);
                    sb.append(".tmx");
                   // map = new mapClass(sb.toString(), type, north, west, south, east, i, j);
                   // this.mapConstructorList.add(map);
                }
            }
        }
        //fill mapConstructorList
    }

    public mapClass getRoom(int x, int y) {
        mapClass map;
        for (int i = 0; i < this.mapConstructorList.size(); i++) {
            map = this.mapConstructorList.get(i);
            if (map.x == x && map.y == y) return map;
        }
        return null;
    }
 }
