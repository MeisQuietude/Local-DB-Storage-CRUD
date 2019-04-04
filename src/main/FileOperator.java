package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class FileOperator {
    final private static String storageDir = "./storage/";

    /* Gets */

    public static String getStorageDir() {
        return storageDir;
    }

    /* **** */

    static class Basic {

        public static boolean createFiles(String[] files) {
            for (String file : files) {
                try {
                    File f = new File(storageDir + file.toLowerCase());
                    if (f.exists()) f.delete();
                    f.createNewFile();
                } catch (IOException e) {
                    return false;
                }
            }
            return true;
        }

        public static boolean writeIntoFile(String filename, String text) {
            return writeIntoFile(new File(filename), text);
        }

        public static boolean writeIntoFile(File filename, String text) {
            return writeIntoFile(filename, text,false);
        }

        public static boolean writeIntoFile(String filename, String text, boolean append) {
            return writeIntoFile(new File(filename), text, append);
        }

        public static boolean writeIntoFile(File filename, String text, boolean append) {
            new File(storageDir).mkdir();

            try {
                FileWriter fw = new FileWriter(storageDir + filename, append);
                fw.write(text);
                fw.close();
            } catch (IOException e) {
                Data.Processing.throwError("Can't write info file: " + filename.getName());
                return false;
            }

            return true;
        }


        public static ArrayList<String> readFile(String filename) {
            return readFile(new File(storageDir + filename.toLowerCase()));
        }

        public static ArrayList<String> readFile(File filename) {
            Scanner reader = null;
            try {
                reader = new Scanner(new FileReader(filename));
            } catch (FileNotFoundException e) {
                Data.Processing.throwError("Can't read file: " + filename.getName());
            }
            assert reader != null;

            ArrayList<String> lines = new ArrayList<>();

            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }

            return lines;
        }

        public static void deleteFromFile(String filename, int line) {
            deleteFromFile(new File(filename), line);
        }

        public static void deleteFromFile(File filename, int line) {
            ArrayList<String> fileText = readFile(filename);
            assert fileText != null;

            fileText.remove(line);

            String newFileText = Additional.convertStrListToString(fileText);
            writeIntoFile(filename, newFileText);
        }
    }

    static class Additional {
        static boolean exists(String file) {
            File f = new File(file.toLowerCase());
            return f.isFile() && f.exists();
        }

        static long calculateStorageLength() {
            File dir = new File(storageDir);
            File[] dirs = dir.listFiles();

            if ((dirs != null ? dirs.length : 0) < 1) return 0;

            long bytes = 0;
            for (File f : dirs) {
                if (f.getName().equals(".metadata")) continue;
                bytes += f.length();
            }
            return bytes;
        }

        static boolean validateMetaFile() {
            new File(storageDir).mkdir();
            File meta = new File(storageDir + ".metadata");

            if (!meta.exists()) return false;

            Data.Metadata.updateMetaFile();

            return true;
        }

        static int[] convertListToArrayInt(ArrayList<Integer> list) {
            int[] result = new int[list.size()];
            for (int i = 0, len = list.size(); i < len; i++) {
                result[i] = list.get(i);
            }
            return result;
        }

        static String convertStrListToString(ArrayList<String> list) {
            StringBuilder newString = new StringBuilder();

            for (String line : list) {
                newString.append(line);
            }

            return newString.toString();
        }

        /**
         * @param filename name of file
         * @return T <Int> count of lines in file
         */
        static int countingNumberOfLinesInFile(File filename) {
            return Basic.readFile(filename).size();
        }
        static int countingNumberOfLinesInFile(String filename) {
            return countingNumberOfLinesInFile(new File(filename));
        }

    }
}
