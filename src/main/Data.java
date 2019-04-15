package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Data {
    static class Metadata {
        private static final String NAME_APP = "Java CRUD-Application";
        private static final String UNIVERSITY = "USPU";
        private static final String AUTHOR = "Stepan Savelyev";
        private static final String VERSION = "0.9";

        private static String metaInfo = buildMetaInfo();
        private static int metaLength = metaInfo.getBytes().length;

        static final String[] FILES = {      // Storage files
                ".metadata",                 // T <String> Config File
                "_id",                       // T <String> Special String
                "name",                      // T <String>
                "price",                     // T <Int32>
                "available",                 // T <Boolean>
                "cities"                     // T <String[]>
        };

        static final String[] ATTRIBUTES = { // Like columns in the table
                "name",                      // T <String>
                "price",                     // T <Int32>
                "available",                 // T <Boolean>
                "cities"                     // T <String[]>
        };


        private static String buildMetaInfo() {
            return String.format(
                    "" +
                            "NAME: %s\n" +
                            "AUTHOR: %s\n" +
                            "VERSION: %s\n" +
                            "UNIVERSITY: %s\n" +
                            "STORAGE: %s\n" +
                            "BYTES: %s\n",
                    NAME_APP, AUTHOR, VERSION, UNIVERSITY,
                    FileOperator.getStorageDir(), FileOperator.Additional.calculateStorageLength()
            ).trim();
        }

        static int getMetaLength() {
            return metaLength;
        }

        static void setMetaLength() {
            Metadata.metaLength = metaInfo.getBytes().length;
        }

        static String getMetaInfo() {
            return metaInfo;
        }

        static void setMetaInfo() {
            Metadata.metaInfo = buildMetaInfo();
        }

        static void updateInfo() {
            setMetaInfo();
            setMetaLength();
        }

        static void updateMetaFile() {
            Metadata.updateInfo();
            FileOperator.Basic.writeIntoFile(".metadata", Metadata.getMetaInfo());
        }

        static String genID() {
            int MIN = 97, MAX = 122;
            int K_MIN = 111, K_MAX = 11111;

            StringBuilder id = new StringBuilder();

            // ID - main
            for (int i = 0; i < 8; i++) {
                id.append((char) (new Random().nextInt((MAX - MIN) + 1) + MIN));
            }
            // ID - key
            id
                    .append("-")
                    .append(new Random().nextInt((K_MAX - K_MIN) + 1) + K_MIN);

            return id.toString();
        }

    }

    public static class Processing {
        static void throwError(String message) {
            System.out.printf("\nThere is an error :: %s :: Stop executing\n", message);
            System.exit(1);
        }

        static void createDB() {
            boolean valid = true;

            System.out.println("Creates new database...");

            valid = FileOperator.Basic.createFiles(Metadata.FILES);
            if (!valid) throwError("Can't create storage files");

            System.out.println("Configure...");
            Metadata.updateInfo();

            valid = FileOperator.Basic.writeIntoFile(".metadata", Metadata.getMetaInfo());
            if (!valid) throwError("Can't configure storage");

            System.out.println("Done.");
        }

        static void insertOne(ArrayList<String> document) {
            assert document.size() == Metadata.ATTRIBUTES.length;

            // ID
            FileOperator.Basic.writeIntoFile("_id", Metadata.genID(), true);

            // Main
            for (int i = 0, len = document.size(); i < len; i++) {
                String text = document.get(i);
                String file = Metadata.ATTRIBUTES[i];

                FileOperator.Basic.writeIntoFile(file, text, true);
            }
        }

        static void searchInStorage() {
            HashMap<String, ArrayList<String>> allDocuments = FileOperator.Additional.getAllDocuments();

            int l = FileOperator.Additional.countLinesInFile("_id");

            for (int i = 0; i < FileOperator.Additional.countLinesInFile("_id"); i++) {
                System.out.printf("\t%d) %s\n", i + 1, getDocumentRow(i));
            }
        }

        public static void searchInStorage(String params) {

        }

        static String getDocumentRow(int index) {
            StringBuilder result = new StringBuilder();
            String[] line = new String[Metadata.ATTRIBUTES.length];

            // [name, price, available, cities]
            for (int i = 0; i < Metadata.ATTRIBUTES.length; i++) {
                line[i] = FileOperator.Basic.readFile(Metadata.ATTRIBUTES[i]).get(index);
            }

            result.append(String.format("%-30s %5s руб., is available: %s, %s",
                            line[0], line[1], line[2], line[3]));

            return result.toString();
        }
    }
}
