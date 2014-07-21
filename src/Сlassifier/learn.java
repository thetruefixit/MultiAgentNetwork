package Сlassifier;

import vk.FileWorker;
import vk.VkWall;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Fixit on 04.06.2014.
 */
public class learn {
    public static types.diffClass getRandomPeople(int Count) throws IOException {
        types.diffClass result = new types.diffClass(Count);
        int i = 0;
        while(i<Count) {
            String id = helper.generateId(1, 200000000);
            String token = getToken();
            VkWall.result wall = VkWall.getWallById(id, token);
            int size = wall.items.size()+wall.hist.size();
            //System.out.println("Получено сообщений с текстом:"+size);
            if((wall.error_code == 0)&(size>50)&(size < 2000)) {
                result.concatination(helper.generateWords(wall));
                i++;
                System.out.println("Получил "+(i)+" пользователя!");
            }
        }
        return result;
    }
    public static types.diffClass getProgrammerList(int Count) throws IOException {
        types.diffClass result = new types.diffClass(Count);
        int i = 0;
        ArrayList<String> ids = FileWorker.read2("C://ids.txt");
        while(i < Count) {
            String token = getToken();
            String id = helper.getRandomGroupId(ids);
            VkWall.result wall = VkWall.getWallById(id, token);
            int size = wall.items.size()+wall.hist.size();
            if((wall.error_code == 0)&(size>50)&(size < 2000)) {
                result.concatination(helper.generateWords(wall));
                i++;
                System.out.println("Получил "+(i)+" пользователя!");
            }
        }
        return result;
    }
    public static String getToken() {
        ArrayList<String> token = new ArrayList<String>();
        token.add("4b9b61404c950be22b29cd78b2f2f937949ed43edf155dd4da737d87a4db6ede271eb011500039a10487c");
        token.add("c507d0dc1098aa7aa58fd204792f8a61e609af2969a2e31fb7b6c3cd757b896f2012368cd06b3d43a5903");
        token.add("ff3268281e3a246ca1221f28ee4d50dab71fab123bf7e6edb52e4aecd074e05a50a5946c2ef1355a072a1");
        token.add("ebbdd31b68220ba4808e887aa88ce10b1b0143c190cb62128aa4760b58d52032ca5820c475d5cd02960eb");
        token.add("15f9366e0ec0c980449933e6436f7dbd89ef8320eeab80eecaf1ada3a721ac960db10529e1bc0c328365a");
        token.add("e11444cadde61ab887797608cca37fde94764583b652acac3a95580c267387f74df780fc66ff3ed02cd11");
        int idint = 1 + (int)(Math.random() * ((5 - 1) + 1));
        return token.get(idint);
    }
    public static types.diffClass WallToWords(VkWall.result input) {
        types.diffClass result = new types.diffClass(1);
            result.concatination(helper.generateWords(input));
        return result;
    }

}
