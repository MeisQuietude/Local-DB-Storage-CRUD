package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public static class Metadata {
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

        public static int getMetaLength() {
            return metaLength;
        }

        public static void setMetaLength() {
            Metadata.metaLength = metaInfo.getBytes().length;
        }

        public static String getMetaInfo() {
            return metaInfo;
        }

        public static void setMetaInfo() {
            Metadata.metaInfo = buildMetaInfo();
        }

        public static void updateInfo() {
            setMetaInfo();
            setMetaLength();
        }

        static void updateMetaFile() {
            Metadata.updateInfo();
            FileOperator.Basic.writeIntoFile(".metadata", Metadata.getMetaInfo());
        }

        static String genID() {
            int MIN = 97, MAX = 122;
            int K_MIN = 11, K_MAX = 99;

            StringBuilder id = new StringBuilder();

            // ID - main
            for (int i = 0; i < 8; i++) {
                id.append((char) ((int) (Math.random() * (MAX + MIN - 1)) + MIN));
            }
            // ID - key
            id
                    .append("-")
                    .append((int) (Math.random() * (K_MAX + K_MIN - 1)) + K_MIN);

            return id.toString();
        }

    }

    public static class Processing {
        static void throwError(String message) {
            System.out.printf("\nThere is an error :: %s :: Stop executing\n", message);
            System.exit(1);
        }

        public static void createDB() {
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

        public static void searchInStorage() {
            HashMap<String, ArrayList<String>> allDocuments = FileOperator.Additional.getAllDocuments();

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

            result.append(String.format("%-50s %5s руб., is available: %1s, %s",
                            line[0], line[1], line[2], line[3]));

            return result.toString();
        }
    }
}
