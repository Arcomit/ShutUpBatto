package com.arcomit.sub.util;

import com.arcomit.sub.ShutUpBattoMod;

import java.io.*;

/**
 * @author til
 */
public class IOUtil {
    /***
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(File file) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                stringBuilder.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            ShutUpBattoMod.LOGGER_4.error(e);
            return "";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    ShutUpBattoMod.LOGGER_4.error(e);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void writer(File file, String text) {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                ShutUpBattoMod.LOGGER_4.error(e);
                return;
            }
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(text);
        } catch (IOException e) {
            ShutUpBattoMod.LOGGER_4.error(e);
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                ShutUpBattoMod.LOGGER_4.error(e);
            }
        }
    }

}
