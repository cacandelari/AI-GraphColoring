/*
* Used the Graph coloring wikipedia article:
* “Graph Coloring.” Wikipedia, Wikimedia Foundation, 7 Feb. 2020, en.wikipedia.org/wiki/Graph_coloring.
* */

import java.io.*;
import java.util.ArrayList;

public class MapColor {

    public static void main(String[]args) {

        /*
        * The csv files MUST be in the program folder with the .idea, out and src files.
        * To change the file being used:
        * change the command line argument
        * Map of Australia = Aus.csv
        * Map of India = India.csv
        * Map of the US = US.csv
        *
        * for my runs, but the files can be named whatever as long as they're in the arg[0] slot.
        * */

        //String csvFile = "C:\\Users\\colin\\IdeaProjects\\Data Structures\\MapColorProblem\\US.csv";//don't know why it got thrown into my data structures folder
        String csvFile = args[0];
        BufferedReader buffread = null;
        String row = "";
        String splitter = ",";

        ArrayList<String> Names = new ArrayList<String>();
        ArrayList<String> OneDim = new ArrayList<String>();

        int count = 0; //Count to use later when creating array sizes
        try {
            buffread = new BufferedReader(new FileReader(csvFile));
            while ((row = buffread.readLine()) != null) {
                int j = 0;
                String[] states = row.split(splitter);

                for (int i = 0; i < states.length; i++) {
                    //System.out.println("State: " + count + ": " + country[i]);
                    OneDim.add(states[i]);//Dump everything into a list to sort later
                }
                count++;
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(buffread!=null){
                try{
                    buffread.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }//File error handling
        //System.out.println("--///////-");

        String[][] Grid = new String[count-1][count-1];//Grid to hold the adjacency list
        String[] arrNames = new String[count - 1];//To hold the names for documentation later
        int[] colorInfo = new int[count - 1];
        colorInfo[0] = 0;

        /*
        * Color info:
        * 4 Colors:
        * Red, Green, Blue, Yellow, None
        * 1  , 2    , 3   , 4     , 5
        * Respectively.
        *
        * The 5th color, none, is required to find solutions for the maps of India and the US
        * */

        ArrayList<String> Temp = new ArrayList<String>();

        for(int i = 0; i < OneDim.size(); i++){
            if(OneDim.get(i).equals("0") || OneDim.get(i).equals("1") || OneDim.get(i).equals(",")){
                //System.out.print(OneDim.get(i) + " ");
                //System.out.print("i: "+i+" ");
                //System.out.print("-");

                Temp.add(OneDim.get(i));//Sorting only the adjacency list into this list to be put into a 2d array

            }
            else {
                //System.out.println(OneDim.get(i));
                //System.out.println(OneDim.get(i) + ", ");
                Names.add(OneDim.get(i));//Throwing the names into the Names list to be put into a 1d name array
                //System.out.println();
            }
            //System.out.println();
        }
        //System.out.println("\n---");

        for(int i = 1; i < count; i++){
            //System.out.println(Names.get(i));
            arrNames[i-1] = Names.get(i);//Names starts at 1 and arrNames starts at 0 to avoid the blank space at the beginning of names at 0,0.
            //System.out.println(arrNames[i-1]);
        }

        //System.out.println(Temp);
        int counter = 0;
        //System.out.println("--");
        for(int x = 0; x < Grid.length; x++){
            for(int y = 0; y < Grid.length; y++){
                //System.out.print(Temp.get(counter) + " ");
                //System.out.println(counter);
                Grid[x][y] = Temp.get(counter);//Placing all of the Adjacency list info into an array for easier use
                //System.out.println("Grid["+x+"]["+y+"]: " + Grid[x][y]);
                counter++;
            }
        }

        /*
        for(int i = 0; i < Grid.length; i++){
            System.out.print(arrNames[i] + " is adjacent to: ");
            for(int j =0; j < Grid.length; j++){
                if(Grid[i][j].equals("1")){
                    //System.out.print("Grid: "+Grid[i][j]);
                    System.out.print(arrNames[j]+", ");
                }
            }
            System.out.println();
        }*/ //checking adjacency

        /*
        for(int i = 0; i < 4; i++){
            System.out.println("i: " + i);
        }//Just error checking to look through the grid and array to check for adjacency when printing later
        */

        ColorStates(0, 0, Grid, colorInfo);

        for(int i = 0; i < colorInfo.length; i++){
            //System.out.println("Colors " + i +": " + colorInfo[i]);// printing the answer AFTER the function has run
        }//error checking to see the answer

        try {

            File output = new File("output.txt");

            FileWriter write = new FileWriter(output);
            BufferedWriter buff = new BufferedWriter(write);

            for (int i = 0; i < Grid.length; i++) {
                buff.write(arrNames[i] + ":" + colorInfo[i] + "->");
                //System.out.print(arrNames[i] + ":" + colorInfo[i] + "->");
                for (int j = 0; j < Grid.length; j++) {
                    if (Grid[i][j].equals("1")) {
                        //System.out.print("Grid: "+Grid[i][j]);
                        //System.out.print(arrNames[j]+", ");
                        //System.out.print(arrNames[j] + ":" + colorInfo[j] + ", ");
                        buff.write(arrNames[j] + ":" + colorInfo[j] + ", ");
                    }
                }
                //System.out.println();
                buff.newLine();
            }//checking adjacency
            buff.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }


    }//end of main

    public static boolean colorCheck(int stateNum, int color, String[][]Grid, int []colorInfo){
        /*
        * This function is used inside of ColorStates to check and see if the states adjacent to the current state being checked are of the same color
        * If they are not the same color then we return true, if they are the same color, we return false.
        * */
        for(int i = 0; i < Grid.length; i++){
            if(Grid[stateNum][i].equals("1") && color == colorInfo[i]){//if the state is adjacent and is the same color
                return false;
            }
        }
        return true;//we're good and can set the color.
    }

    public static void ColorStates(int stateNum, int color, String[][]Grid, int[] colorInfo){

        for(color = 1; color < 5; color++){//starts at 1 because I'm considering the first color to be 1, not 0.
            if(colorCheck(stateNum, color, Grid, colorInfo)){//If true, keep going, else exit
                //System.out.println("It's good");//error checking
                colorInfo[stateNum] = color;//setting the colors
                if(stateNum + 1 < Grid.length){//If there are still states left, keep going, else stop and print the answer
                    ColorStates(stateNum + 1, color, Grid, colorInfo);//recurse
                }
            }
        }
    }
}
