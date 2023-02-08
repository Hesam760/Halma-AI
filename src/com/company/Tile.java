package com.company;

public class Tile {
    byte x;
    byte y;
    // 0 is empty 1 is red and 2 is blue
    byte color = 0;

    // 0 is none, 1 is red zone and 2 is blue zone
    byte zone = 0;
    public Tile(byte x,byte y){
        this.x = x;
        this.y = y;
    }
}
