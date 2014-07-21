import madkit.kernel.Madkit;
import vk.FileWorker;
import vk.VkMessages;
import Сlassifier.helper;
import Сlassifier.learn;
import Сlassifier.types;

import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;

//132331350 - Деля ; 20044893 - Ира
class AffableThread extends Thread
{
    @Override
    public void run()	//Этот метод будет выполнен в побочном потоке
    {
        types.diffClass temp;
        try {
            temp = learn.getProgrammerList(100);
            temp.printCount();
            temp.saveTxt("C://programmer/temp" + helper.generateId(1,1000000)+".txt");
           // System.out.println(this.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
public class Main {
    static AffableThread[] mass;
    public static void main(String[] args) throws IOException {
//       new Madkit("--launchAgents","Agent.Boss,true,1;Agent.Getter,true,1;Agent.Server,true,1;Agent.Includer,true,1;Agent.Analize,true,1");
       // new Madkit("--launchAgents","Agent.Boss,true;Agent.Server,true");
//        String token = Token.GetToken();
//        System.out.println(token);
//        types.diffClass simple = new types.diffClass(2460);
//        simple.loadBD("simplePeople");
//        types.diffClass programmer = new types.diffClass(1900);
//        types.diffClass currentUser = new types.diffClass(1);
//        types.diffClass uniq = new types.diffClass(simple.CountUsers+programmer.CountUsers);
//        simple.loadBD("simplePeople");
//        programmer.loadBD("programmer");
//        uniq.loadBD("uniq_learn");
//        simple.printCount();
//        programmer.printCount();
//        uniq.printCount();
//        VkWall.result test = VkWall.getWallById("124262416",token);
//        currentUser = learn.WallToWords(test);
//        currentUser.printCount();
//        simple.addToList("предоставляю");
//        simple.addToList("услуги");
//        simple.addToList("бухгалтера");
//        simple.addToList("спешите");
//        simple.addToList("купить");
//        simple.addToList("виагру");
//        programmer.addToList("надо");
//        programmer.addToList("купить");
//        programmer.addToList("молоко");
//        currentUser.addToList("надо");
//        currentUser.addToList("купить");
//        currentUser.addToList("сигареты");
//        double result1 = computing.getCoef(simple, currentUser, simple.CountUsers + programmer.CountUsers, uniq.words.size());
//        double result2 = computing.getCoef(programmer, currentUser, simple.CountUsers + programmer.CountUsers, uniq.words.size());
//        System.out.println(result1+" = %"+computing.getPersentFromLog(result1,result2));
//        System.out.println(result2+" = %"+computing.getPersentFromLog(result2,result1));
        String token = "394c0e1d1f9bde373ad8d05c51e04c8876e27454a1a6c726c54ac954a21ecc400f0b0159f553c8f6d7d69";
        String msg = "";
        ArrayList<VkMessages.items> mass = VkMessages.getAllMsgById("20044893",token);
        for (int i = 0; i < mass.size();i++) {
            if(mass.get(i).from_id.equals("15631680")) {
                msg +="Даниил Белевцев:" + System.lineSeparator();
                msg +=mass.get(i).body + System.lineSeparator();
                msg += System.lineSeparator();
            }
            else {
                System.out.println(mass.get(i).from_id);
                msg += "Ирина Еретенко:" + System.lineSeparator();
                msg += mass.get(i).body + System.lineSeparator();
                msg += System.lineSeparator();
            }
        }
        //System.out.println(msg);
        FileWorker.write("C://msg.txt",msg);
    }
}