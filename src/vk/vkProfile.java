package vk;

import java.util.ArrayList;

public class vkProfile {

        public static class response {
            public String id="";//id фото
            public String first_name="";
            public String last_name="";
            public String sex="";
            public String screen_name="";
            public String bdate="";
            public city city = new city();
        }
        public static class city {
            String id = "";
            public String title = "";
        }
//        public static class response {
//            @SerializedName("")public items items = new items();
//        }
        public static class ProfileJson {
            public int count = 0;
            public response[] response = new response[0];
        }
        private static String ArrayListToStr(ArrayList<String> arr) {
            String res ="";
            for (int i = 0; i < arr.size(); i++) {
                if(i!=arr.size()-1)
                res=res+arr.get(i)+",";
            }
            return res;
        }
//        public static ProfileJson getProfileByFriendList(String token) throws IOException {
//            ArrayList<String> list = vk.VkFriends.getFriendsList(token);
//            String Response = HttpGetter.sendGet("users.get?user_ids="+ArrayListToStr(list)+"&fields=sex,bdate,city,screen_name&v=5.8",token);
//            //System.out.println(Response);
//            Gson gson = new Gson();
//            ProfileJson obj = gson.fromJson(Response,ProfileJson.class);
//            obj.count = list.size();
//            return obj;
//        }

}
