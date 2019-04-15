package main;

import java.util.ArrayList;
import java.util.Scanner;

class User {
    static class UserInterface {

        void selectMainAction() {
            /* CARE :: USER INTERFACE */
            Scanner in = new Scanner(System.in);

            while (true) {
                Show.mainActions();
                System.out.print("> ");
                String userInput = Action.getUserInputLineFirst(in);

                if (userInput == null) continue;

                switch (userInput) {
                    case "1":
                    case "create":
                        Action.Main.actionCreate(in);
                        break;

                    case "2":
                    case "use":
                        selectUseAction();
                        break;

                    case "0":
                    case "exit":
                    case "quit":
                        in.close();
                        return;

                    default:
                        System.out.println("Command does not exist");
                }
            }
        }

        void selectUseAction() {
            Scanner in = new Scanner(System.in);

            while (true) {
                Show.useActions();
                System.out.print("> ");
                String userInput = Action.getUserInputLineFirst(in);

                if (userInput == null) continue;

                switch (userInput) {
                    case "1":
                    case "create":
                    case "new":
                        Action.Use.appendNewDocument(in);
                        break;

                    case "read":
                    case "2":
                        Data.Processing.searchInStorage();
                        break;

                    case "update":
                    case "3":
                        Action.Use.updateOneDocument(in);
                        break;

                    case "delete":
                    case "4":
                        Action.Use.deleteOneDocument(in);
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
            static void actionCreate(Scanner in) {
                System.out.print("Are you sure? Y/N\t");
                String userInput = Action.getUserInputLineFirst(in);

                if (userInput != null && userInput.startsWith("y")) {
                    Data.Processing.createDB();
                } else {
                    System.out.println("Create new DB cancelled");
                }
            }
        }

        static class Use {
            static void appendNewDocument(Scanner in) {
                ArrayList<String> result = new ArrayList<>();

                for (String attribute : Data.Metadata.ATTRIBUTES) {
                    String userInput = null;
                    while (userInput == null) {
                        System.out.print(attribute + ": ");
                        userInput = Action.getUserInputLine(in);
                        assert userInput != null;

                        if (userInput.equals("-1")) return;

                        if (!userInput.trim().equals("")) {
                            System.out.printf("%s, right? Y/N\t", userInput);
                            String userAnswer = Action.getUserInputLineFirst(in);
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
                System.out.println("Inserted line: " + FileOperator.Additional.convertStrListToString(result));
            }

            static void deleteOneDocument(Scanner in) {
                int index = getUserIndexOfLine(in);
                if (index == -1) return;

                FileOperator.Basic.deleteFromFile("_id", index);
                for (String attribute : Data.Metadata.ATTRIBUTES) {
                    FileOperator.Basic.deleteFromFile(attribute, index);
                }
                System.out.println("Done");
            }

            static void updateOneDocument(Scanner in) {
                int index = getUserIndexOfLine(in);
                if (index == -1) return;

                String userInput = null;

                System.out.println(Data.Processing.getDocumentRow(index));

                while (true) {
                    Show.updateActions();
                    userInput = getUserInputLine(in);
                    if (userInput == null) continue;

                    String newParam;
                    switch (userInput) {
                        case "0":
                            return;
                        case "1":  // Name
                            System.out.print("new name: ");
                            newParam = getUserInputLine(in);
                            FileOperator.Basic.replaceInStorage("name", index, newParam);
                            break;
                        case "2":  // price
                            System.out.print("new price: ");
                            newParam = getUserInputLine(in);
                            FileOperator.Basic.replaceInStorage("price", index, newParam);
                            break;
                        case "3":  // Available
                            System.out.print("is available (0/1): ");
                            newParam = getUserInputLine(in);
                            FileOperator.Basic.replaceInStorage("available", index, newParam);
                            break;
                        case "4":  // Cities
                            System.out.print("new cities: ");
                            newParam = getUserInputLine(in);
                            FileOperator.Basic.replaceInStorage("cities", index, newParam);
                            break;
                        default:
                            System.out.println("Does not exist attribute");
                    }
                }
            }
        }


        static String getUserInputLineFirst(Scanner in) {
            if (in.hasNextLine())
                return in.nextLine().toLowerCase().trim().split(" +")[0];
            return null;
        }

        static String getUserInputLine(Scanner in) {
            if (in.hasNextLine())
                return in.nextLine().trim();
            return null;
        }

        static int getUserIndexOfLine(Scanner in) {
            int index = -1;
            while (index < 1) {
                String tmp;

                System.out.print("Number of line: ");
                tmp = getUserInputLineFirst(in);

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
                    break;
                }

                return index;
            }
            return -1;
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
