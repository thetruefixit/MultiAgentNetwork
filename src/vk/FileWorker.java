package vk;

import java.io.*;
import java.util.ArrayList;

/**
 * Данный класс предназначен для упрощение работы с файловыми структурами в программе.
 * Основная его задача Ввод/Вывод из/в  текстовых(-ые) файлов(-ы).
 */
public class FileWorker {
    public static void write(String fileName, String text) {
        //Определяем файл
        File file = new File(fileName);
        try {
            //проверяем, что если файл не существует то создаем его
            if(!file.exists()){
                file.createNewFile();
            }
            //PrintWriter обеспечит возможности записи в файл
            FileWriter out = new FileWriter(file.getAbsoluteFile(),true);

            try {
                //Записываем текст в файл

                out.write(text);
            } finally {
                //После чего мы должны закрыть файл
                //Иначе файл не запишется
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<String> read2(String fileName) {    // Метод чтения, который позволяет считывать сразу в список
        ArrayList<String> Content = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader( new File(fileName).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    Content.add(s);
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return Content;
    }
    public static String read(String fileName) {  //Стандартное считывание в строку.
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader( new File(fileName).getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
