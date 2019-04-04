package main;

import java.io.File;

public class Data {
    public static class Metadata {
        private static final String NAME_APP = "Java CRUD-Application";
        private static final String UNIVERSITY = "USPU";
        private static final String AUTHOR = "Stepan Savelyev";
        private static final String VERSION = "0.9";

        private static String metaInfo = buildMetaInfo();
        private static int metaLength = metaInfo.getBytes().length;

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

    }

    public static class Processing {
        static void throwError(String message) {
            System.out.printf("\nThere is an error :: %s :: Stop executing\n", message);
            System.exit(1);
        }

        public static void createDB() {
            boolean valid = true;

            final String[] ATTRIBUTES = {
                    ".metadata",          // T <String> Config File
                    "_id",                // T <String> Special String
                    "product_name",       // T <String>
                    "price",              // T <Int32>
                    "available",          // T <Boolean>
                    "cities"              // T <String[]>
            };

            valid = FileOperator.Basic.createFiles(ATTRIBUTES);
            if (!valid) throwError("Can't create storage files");

            Metadata.updateInfo();

            valid = FileOperator.Basic.writeIntoFile(".metadata", Metadata.getMetaInfo());
            if (!valid) throwError("Can't configure storage");
        }

        public static void searchInStorage(String params) {

        }
    }
}
