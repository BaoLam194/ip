import java.util.Scanner;

public class Nuke {
    private static void greet(){
        System.out.println("\t===============================");
        System.out.println("\tHi! Nuke's waiting for your command!");
        System.out.println("\tWho do you want me to nuke today?");
        separate();
    }

    private static void exit(){
        System.out.println("\tKaboommm!");
        System.out.println("\tI have destroyed our current session!");
        System.out.println("\tSee you later!");
        System.out.println("\t===============================");
    }

    private static void separate(){
        System.out.println("\t-------------------------------");
    }

    private static boolean handle(String line){
        if(line.equals("bye")){
            separate();
            exit();
            return false;
        }
        separate();
        System.out.printf("\t%s%n",line);
        separate();
        return true;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        greet();
        // Hand over execution for handle()
        while(handle(in.nextLine())){
        }
    }
}
