package Сlassifier;

import java.io.IOException;

/**
 * Created by Fixit on 06.06.2014.
 */
public class computing {
    public static double getCoef(types.diffClass list1,types.diffClass currentUser,double All_User_Count,double Uniq_word_count) throws IOException {
        Double result = 0.0;
        double Dc = list1.CountUsers;
        result += Math.log(Dc/All_User_Count);
        for (int i = 0; i < currentUser.words.size();i++) {
            double pos = list1.getCount(currentUser.words.get(i).text);
            double listSize = list1.words.size()    ;
            result += Math.log((pos + 1.0)/(Uniq_word_count + listSize));
        }
        return result;
    }
    private static double max(double one, double two) {
        if(one>two)
            return two;
        else
            return one;
    }
    public static double getPersentFromLog(double one,double two) {
        int max = (int) max(one,two);
        one -= max;
        two -= max;
        double result = (Math.exp(one)/(Math.exp(one)+Math.exp(two)));
        return result;
    }
}
