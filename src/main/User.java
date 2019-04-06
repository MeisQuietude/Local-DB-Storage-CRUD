package main;

import java.util.Scanner;

public class User {
    static class UserInterface {

        void selectAction() {
            /* CARE :: USER INTERFACE */
            Scanner in = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                String userInput = in.nextLine().trim().split(" +")[0];

                switch (userInput) {
                    case "create":
                        break;
                    case "read":
                        Data.Processing.searchInStorage();
                        break;
                    case "update":
                        break;
                    case "delete":
                        break;

                    case "exit":
                    case "quit":
                        in.close();
                        return;

                    default: System.out.println("Command not exists");
                }
            }
        }
    }
}
