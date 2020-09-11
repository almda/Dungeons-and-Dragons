
import View.cmdPrinter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Controller con; //Game Controller
    static cmdPrinter ui; //User Interface
    static Scanner reader= new Scanner(System.in);

    public static void main(String[] args) {
        startupPrints();
        int charSelected = reader.nextInt();
        ArrayList<Board> levelsBoards = new ArrayList<>();
        try{
            List<String> levels= Files.list(Paths.get(args[0])).sorted().map(Path::toString).collect(Collectors.toList()); //translate all levels from path
            for(String level:levels)
                levelsBoards.add(new Board(toBoard(level),charSelected));
        }
        catch (IOException e) { System.out.println("Error on loading levels"); }

        int currLevelCounter=0;

        //Main Game Loop
        con = new Controller(levelsBoards.get(0));
        cmdPrinter.sendMessage(con.getBoard().toString());
        while (true){
            if(con.won && currLevelCounter<levelsBoards.size()-1) { //won current level and there are more levels to play
                currLevelCounter++;
                cmdPrinter.sendMessage("You won the stage! Staring stage"+ (currLevelCounter+1) +"\n");
                int lvl = con.player.getLevel(); int exp = con.player.getExperience();
                con = new Controller(levelsBoards.get(currLevelCounter));
                keepPlayerInfo(con,lvl,exp);
                Clock.GameTick=0;
                cmdPrinter.sendMessage(con.getBoard().toString());
            }
            String move = reader.next();
            if(move.length()!=1) {cmdPrinter.sendMessage("bad input"); continue;}
            con.Turn(move);
            cmdPrinter.sendMessage(con.getBoard().toString());
            if(con.isDead())break;
        }
    }

    private static char[][] toBoard(String level) {
        List<String> lines=Collections.EMPTY_LIST;
        try
        {
            lines=Files.readAllLines(Paths.get(level));
            char [][] board= new char [lines.size()] [longestRowLength(lines)];
            int rowPlace = 0;
            for (String row : lines) {
                for (int i = 0; i < row.length(); i++) {
                    char c = row.charAt(i);
                    board[rowPlace][i] = c;
                }
                rowPlace++;
            }
            return board;
        }
        catch (IOException e)
        {
            System.out.println("Some stage is not ok...");
            System.exit(-1);
        }
        return null;
    }

    public static int longestRowLength(List<String> lines)
    {
        int a=0;
        for(String row:lines)
        {
            if(row.length()>a)
                a=row.length();
        }
        return a;
    }

    //make sure when level up the player status stays the same
    public static void keepPlayerInfo(Controller newCon, int lvl, int exp){
        newCon.player.setLevel(lvl);
        newCon.player.setExperience(exp);
    }

    public static void startupPrints(){
        System.out.println("\n" +
                "██████╗░██╗░░░██╗███╗░░██╗░██████╗░███████╗░█████╗░███╗░░██╗░██████╗  ░█████╗░███╗░░██╗██████╗░\n" +
                "██╔══██╗██║░░░██║████╗░██║██╔════╝░██╔════╝██╔══██╗████╗░██║██╔════╝  ██╔══██╗████╗░██║██╔══██╗\n" +
                "██║░░██║██║░░░██║██╔██╗██║██║░░██╗░█████╗░░██║░░██║██╔██╗██║╚█████╗░  ███████║██╔██╗██║██║░░██║\n" +
                "██║░░██║██║░░░██║██║╚████║██║░░╚██╗██╔══╝░░██║░░██║██║╚████║░╚═══██╗  ██╔══██║██║╚████║██║░░██║\n" +
                "██████╔╝╚██████╔╝██║░╚███║╚██████╔╝███████╗╚█████╔╝██║░╚███║██████╔╝  ██║░░██║██║░╚███║██████╔╝\n" +
                "╚═════╝░░╚═════╝░╚═╝░░╚══╝░╚═════╝░╚══════╝░╚════╝░╚═╝░░╚══╝╚═════╝░  ╚═╝░░╚═╝╚═╝░░╚══╝╚═════╝░\n" +
                "\n" +
                "██████╗░██████╗░░█████╗░░██████╗░░█████╗░███╗░░██╗░██████╗\n" +
                "██╔══██╗██╔══██╗██╔══██╗██╔════╝░██╔══██╗████╗░██║██╔════╝\n" +
                "██║░░██║██████╔╝███████║██║░░██╗░██║░░██║██╔██╗██║╚█████╗░\n" +
                "██║░░██║██╔══██╗██╔══██║██║░░╚██╗██║░░██║██║╚████║░╚═══██╗\n" +
                "██████╔╝██║░░██║██║░░██║╚██████╔╝╚█████╔╝██║░╚███║██████╔╝\n" +
                "╚═════╝░╚═╝░░╚═╝╚═╝░░╚═╝░╚═════╝░░╚════╝░╚═╝░░╚══╝╚═════╝░");
        System.out.println("Select player:\n 1.Jon Snow\n 2.The Hound\n 3.Melisandre\n 4.Thoros Of Myr\n 5.Arya Stark\n 6.Bronn");
    }

    public static cmdPrinter getUI(){ return ui; }
}
