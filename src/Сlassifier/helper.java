package Сlassifier;

import vk.VkWall;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Fixit on 04.06.2014.
 */
public class helper {
    public static String deleteAllSymbols(String input) {
        String result = "";
        input = input.toLowerCase();
        for (int i = 0;i<input.length();i++) {
            if(((input.charAt(i)>='a')&(input.charAt(i)<='z'))|(input.charAt(i)==' ')|((input.charAt(i)>='а')&(input.charAt(i)<='я'))) {
                result = result + input.charAt(i);
            }
        }
        return result;
    }
    public static String generateId(int Min,int Max) {
        String id = "";
        int idint = Min + (int)(Math.random() * ((Max - Min) + 1));
        id = id + idint;
        return id;
    }
    public static types.diffClass generateWords(VkWall.result input) {
        types.diffClass mass = new types.diffClass(1);
        for (int i = 0;i < input.items.size();i++) {
            String[] parts = helper.deleteAllSymbols(input.items.get(i).text).split(" ");
            for (int j = 0; j<parts.length;j++) {
                mass.addToList(parts[j]);
                //      System.out.print(parts[j]+" ");
            }
            // System.out.println();
        }
        for (int i = 0;i < input.hist.size();i++) {
            String[] parts = helper.deleteAllSymbols(input.hist.get(i).text).split(" ");
            for (int j = 0;j<parts.length;j++) {
                mass.addToList(parts[j]);
                // System.out.print(parts[j]+" ");
            }
            //System.out.println();
        }
        return mass;
    }
    public static String getRandomGroupId(ArrayList<String> ids) throws IOException {
        String result;
        int index = Integer.parseInt(generateId(0,ids.size()));
        result = ids.get(index);
        return result;
    }

}
