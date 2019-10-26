package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

class App { // Акжолов Ренат, Рогачев Антон. ИТ-41
    // "x,y" --> [North, East, South, West]
    HashMap<String, char[]> cells = new HashMap<String, char[]>(); 
    Direction direction = Direction.SOUTH;
    int x = 0;
    int y = 0;

    int minX = 0;
    int maxX = 0;
    int maxY = 1;

    public static void main(String[] args) throws Exception {
        App s = new App();
        s.restoreLabirinths("..//small-test.in.txt", "..//small-test.out.txt");
        s.restoreLabirinths("..//large-test.in.txt", "..//large-test.out.txt");
    }

    public void restoreLabirinths(String inputFile, String outputFile) {

        try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile, true));

            int num = Integer.parseInt(bufferedReader.readLine());

            for (int i = 1; i <= num; i++) {
                String output = doOneLabirinth(bufferedReader.readLine(), i);
                bufferedWriter.write(output);
                resetVars();
            }
            
            bufferedReader.close();
            bufferedWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private String doOneLabirinth(String line, int num) {
        for (String str : line.split(" ")) {
            
            for (int i = 0; i < str.length(); i++) {
                String action = Character.toString(str.charAt(i));
                char[] ways = cells.getOrDefault(Integer.toString(x) + "," + Integer.toString(y), new char[] {'0', '0', '0', '0'});
                // 1 - проход есть, 0 - нет
                if (action.equals("W")) {

                    ways[direction.ordinal()] = '1';

                } else if (action.equals("L")) {
                    
                    ways[(direction.ordinal() + 3) % 4] = '1';
                    direction = direction.changeDirection(action);
                    
                } else if (action.equals("R")) {

                    if (i < str.length() - 1)
                        if (!Character.toString(str.charAt(i+1)).equals("R"))
                            ways[(direction.ordinal() + 1) % 4] = '1'; // если не разворачиваемся, то справа есть проход

                    direction = direction.changeDirection(action);
                }

                cells.put(Integer.toString(x) + "," + Integer.toString(y), ways);
                
                if (action.equals("W")) {
                    switch (direction) {
                        case NORTH:
                            y--;
                            break;
                        case SOUTH:
                            y++;
                            break;
                        case EAST:
                            x++;
                            break;
                        case WEST:
                            x--;
                            break;
                        default:
                            break;
                    }
                }
                if (i != str.length() - 1 && i != 0) {
                    minX = x < minX ? x : minX;
                    maxX = x > maxX ? x : maxX;
                    maxY = y > maxY ? y : maxY;
                }
            }
            direction = direction.oppositeDirection();
        }
        return "Case #" + num + ":\n" + collectString();
    }

    public String collectString() {
        String labirinth = "";

        for (int i = 1; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                labirinth += toHex(cells.get(Integer.toString(j) + "," + Integer.toString(i)));
            }
            labirinth += "\n";
        }
        return labirinth;
    }

    public String toHex(char[] ways) {
        char[] changedWays = new char[] {
            ways[Direction.EAST.ordinal()],
            ways[Direction.WEST.ordinal()],
            ways[Direction.SOUTH.ordinal()],
            ways[Direction.NORTH.ordinal()]
        };
        return Integer.toHexString(Integer.parseInt(new String(changedWays), 2));
    }

    private void resetVars() {
        cells = new HashMap<String, char[]>(); 
        direction = Direction.SOUTH;
        x = 0;
        y = 0;
        minX = 0;
        maxX = 0;
        maxY = 1;
    }
}

enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public Direction oppositeDirection() {
        return Direction.values()[(this.ordinal() + 2) % 4];
    }

    public Direction changeDirection(String action) {
        if (action.equals("L"))
            return Direction.values()[(this.ordinal() + 3) % 4];
        else
            return Direction.values()[(this.ordinal() + 1) % 4];
    } 
}
