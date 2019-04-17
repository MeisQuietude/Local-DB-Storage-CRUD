package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class User {

    private static final Scanner scanner = new Scanner(System.in);

    static class UserInterface {

        void selectMainAction() {
            /* CARE :: USER INTERFACE */

            while (true) {
                Show.mainActions();
                System.out.print("> ");
                String userInput = Action.getUserInputLineFirst();

                if (userInput == null) continue;

                switch (userInput) {
                    case "1":
                    case "create":
                        Action.Main.actionCreate();
                        break;

                    case "2":
                    case "use":
                        selectUseAction();
                        break;

                    case "0":
                    case "exit":
                    case "quit":
                        scanner.close();
                        return;

                    default:
                        System.out.println("Command does not exist");
                }
            }
        }

        void selectUseAction() {

            while (true) {
                Show.useActions();
                System.out.print("> ");
                String userInput = Action.getUserInputLineFirst();

                if (userInput == null) continue;

                switch (userInput) {
                    case "1":
                    case "create":
                    case "new":
                        Action.Use.appendNewDocument();
                        break;

                    case "read":
                    case "2":
                        selectReadAction();
                        break;

                    case "update":
                    case "3":
                        Action.Use.updateOneDocument();
                        break;

                    case "delete":
                    case "4":
                        Action.Use.deleteOneDocument();
                        break;

                    case "back":
                    case "0":
                        return;

                    default:
                        System.out.println("Command does not exist");
                }
            }
        }

        void selectReadAction() {

            while (true) {
                Show.readActions();
                System.out.print("> ");
                String userInput = Action.getUserInputLineFirst();

                if (userInput == null) continue;

                switch (userInput) {
                    case "readall":
                    case "read":
                    case "1":
                        Data.Processing.searchInStorage();
                        break;

                    case "search":
                    case "2":
                        User.Action.Use.searchForDocument();
                        break;

                    case "back":
                    case "0":
                        return;

                    default:
                        System.out.println("Command does not exist");
                }
            }

        }
    }


    static class Action {

        static class Main {
            static void actionCreate() {
                System.out.print("Are you sure? Y/N\t");
                String userInput = Action.getUserInputLineFirst();

                if (userInput != null && userInput.startsWith("y")) {
                    Data.Processing.createDB();
                } else {
                    System.out.println("Create new DB cancelled");
                }
            }
        }

        static class Use {
            static void appendNewDocument() {
                ArrayList<String> result = new ArrayList<>();

                for (String attribute : Data.Metadata.getATTRIBUTES()) {
                    String userInput = null;
                    while (userInput == null) {
                        System.out.print(attribute + ": ");
                        userInput = Action.getUserInputLine();
                        assert userInput != null;

                        if (userInput.equals("-1")) return;

                        if (!userInput.trim().equals("")) {
                            System.out.printf("%s, right? Y/N\t", userInput);
                            String userAnswer = Action.getUserInputLineFirst();
                            if (userAnswer != null && (userAnswer.startsWith("n") || userAnswer.startsWith("N"))) {
                                userInput = null;
                            } else {
                                result.add(userInput);
                            }
                        } else {
                            userInput = null;
                        }
                    }
                }
                Data.Processing.insertOne(result);
                Data.Metadata.updateMetaFile();
//                System.out.println("Inserted line: " + FileOperator.Additional.convertStrListToRowString(result));
            }

            static void deleteOneDocument() {
                int index = getUserIndexOfLine();
                if (index == -1) return;

                FileOperator.Basic.deleteFromFile("_id", index);
                for (String attribute : Data.Metadata.getATTRIBUTES()) {
                    FileOperator.Basic.deleteFromFile(attribute, index);
                }
                Data.Metadata.updateMetaFile();
                System.out.println("Done");
            }

            static void updateOneDocument() {
                int index = getUserIndexOfLine();
                if (index == -1) return;

                String userInput = null;

                System.out.println(Data.Processing.getDocumentRow(index));

                while (true) {
                    Show.updateActions();
                    userInput = getUserInputLine();
                    if (userInput == null) continue;

                    String newParam;
                    switch (userInput) {
                        case "0":
                            return;
                        case "1":  // Name
                            System.out.print("new name: ");
                            newParam = getUserInputLine();
                            FileOperator.Basic.replaceInStorage("name", index, newParam);
                            break;
                        case "2":  // price
                            System.out.print("new price: ");
                            newParam = getUserInputLine();
                            FileOperator.Basic.replaceInStorage("price", index, newParam);
                            break;
                        case "3":  // Available
                            System.out.print("is available (0/1): ");
                            newParam = getUserInputLine();
                            FileOperator.Basic.replaceInStorage("available", index, newParam);
                            break;
                        case "4":  // Cities
                            System.out.print("new cities: ");
                            newParam = getUserInputLine();
                            FileOperator.Basic.replaceInStorage("cities", index, newParam);
                            break;
                        default:
                            System.out.println("Does not exist attribute");
                    }

                    Data.Metadata.updateMetaFile();
                }
            }

         static void searchForDocument() {
             HashMap<String, String> userInput = getQueryInput();
             Data.Processing.searchInStorage(userInput);
         }

        }


        static String getUserInputLineFirst() {
            if (scanner.hasNextLine())
                return scanner.nextLine().toLowerCase().trim().split(" +")[0];
            return null;
        }

        static String getUserInputLine() {
            if (scanner.hasNextLine())
                return scanner.nextLine().trim();
            return null;
        }

        static int getUserIndexOfLine() {
            int index = -1;
            while (index < 1) {
                String tmp;

                System.out.print("Number of line: ");
                tmp = getUserInputLineFirst();

                if (tmp == null) return -1;
                if (tmp.equals("-1")) return -1;

                try {
                    index = Integer.parseInt(tmp) - 1;

                    // WHY ASSERT DOES NOT WORK BLIN ZADOLBALSIA
                    assert index <= FileOperator.Additional.countLinesInStorage() : "Index out of range";

                    if (index > FileOperator.Additional.countLinesInStorage()) {
                        throw(new AssertionError("Index out of range"));
                    }

                } catch (NumberFormatException | AssertionError e) {
                    System.out.println("Incorrect number, try again or type -1 for exit.");
                    index = -1;
                }
            }
            return index;
        }

        static HashMap<String, String> getQueryInput() {
            HashMap<String, String> query = new HashMap<>();

            for (String attribute : Data.Metadata.getATTRIBUTES()) {
                System.out.printf("%s: ", attribute);

                String userInput = getUserInputLine();
                assert userInput != null;

                query.put(attribute, userInput.trim());
            }

            return query;
        }
    }

    static class Show {
        static void mainActions() {
            StringBuilder actions = new StringBuilder();

            actions
                    .append("1. Create\n")
                    .append("2. Use\n")
                    .append("0. Exit");

            System.out.println(actions.toString());
        }

        static void useActions() {
            StringBuilder actions = new StringBuilder();

            actions
                    .append("1. Create\n")
                    .append("2. Read\n")
                    .append("3. Update\n")
                    .append("4. Delete\n")
                    .append("0. Back");

            System.out.println(actions.toString());
        }

        static void readActions() {
            StringBuilder actions = new StringBuilder();

            actions
                    .append("1. Read all\n")
                    .append("2. Search\n")
                    .append("0. Back");

            System.out.println(actions.toString());

        }

        static void updateActions() {
            StringBuilder actions = new StringBuilder();

            actions
                    .append("1. Name\n")
                    .append("2. Price\n")
                    .append("3. Available\n")
                    .append("4. Cities\n")
                    .append("0. Back");

            System.out.println(actions.toString());
        }
    }
}
