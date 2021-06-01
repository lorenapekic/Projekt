package com.riteh.whisk;

import java.util.concurrent.ThreadLocalRandom;

public class levelClass {
    char[][] level;
    boolean check;
    boolean hasExit;
    int si, sj;

    public levelClass() {
        this.level = new char[15][15];
        this.generateLevel();
    }

    public void generateLevel() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        check = false;

        while (!check) {
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    this.level[i][j] = '-';
                }
            }
            this.si = this.sj = 7;
            this.level[this.si][this.sj] = 'S';
            hasExit = false;
            seed(this.si, this.sj, 6, random);

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j <15; j++) {
                    if (this.level[i][j] == 'E') check = true;
                }
            }
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j <15; j++) {
                if (this.level[i][j] == '0') this.level[i][j] = '-';
                if (this.level[i][j] == '1') {
                    Integer stage = ThreadLocalRandom.current().nextInt(0, 10);
                    this.level[i][j] = stage.toString().charAt(0);
                }

            }
        }
    }

    public void seed(int i, int j, int difficulty, ThreadLocalRandom random) {
        int chance;

        if (difficulty == 0) {
            if (!hasExit) {
                this.level[i][j] = 'E';
                hasExit = true;
            } else {
                this.level[i][j] = '1';
                return;
            }
        }

        if (this.level[i+1][j] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (i+1) < 10) {
                this.level[i+1][j] = '1';
                seed(i+1, j, difficulty-1, random);
            } else this.level[i+1][j] = '0';
        }

        if (this.level[i-1][j] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (i-1) > 0) {
                this.level[i-1][j] = '1';
                seed(i-1, j, difficulty-1, random);
            } else this.level[i-1][j] = '0';
        }

        if (this.level[i][j+1] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (j+1) < 10) {
                this.level[i][j+1] = '1';
                seed(i, j+1, difficulty-1, random);
            } else this.level[i][j+1] = '0';
        }

        if (this.level[i][j-1] == '-') {
            chance = random.nextInt(1, 100);
            if (chance > 50 && (j-1) > 0) {
                this.level[i][j-1] = '1';
                seed(i, j-1, difficulty-1, random);
            } else this.level[i][j-1] = '0';
        }
    }

    public void print() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(this.level[i][j] + " ");
            }
            System.out.print('\n');
        }
    }

    public static void main(String []args){
        levelClass novo = new levelClass();
        novo.print();
    }
}