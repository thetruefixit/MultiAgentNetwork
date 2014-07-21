package db;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import vk.VkWall;
import vk.vkPhoto;
import vk.vkProfile;
import yandex.geocode;
import yandex.types;
import Сlassifier.types.diffClass;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class Mysql {
    String user;
    String password;
    String host;
    private static Connection cnt = null;
///////////////////////////////Constructors/////////////////////////////////////

    public Mysql(String user, String password, String host) {
        this.user = user;
        this.password = password;
        this.host = host;
        try {
            cnt = (Connection) DriverManager.getConnection("jdbc:mysql://"+host, user, password);
        } catch (SQLException e) {
            System.out.println("Connection to MySQL Error(1)");
        }
    }
    public Mysql(String user, String password) {
        this.user = user;
        this.password = password;
        try {
            cnt = (Connection) DriverManager.getConnection("jdbc:mysql://localhost", user, password);
        } catch (SQLException e) {
            System.out.println("Connection to MySQL Error(1)");
        }
    }
////////////////////////////////////////////////////////////////////////////////

    public static String getMsgById(String id) {
        String msg = "";
        try {
            PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("select body from test.messages where id = ?");
            pst.setString(1, id);//установление значения параметра. Обратите внимание: нумерация параметров начинается не с 0, а с 1!
            // System.out.println(pst.getPreparedSql());
            ResultSet prs = pst.executeQuery();
            try {
                while(prs.next()) {
                    msg=prs.getString("body");
                }
            } catch (SQLException e) {
                msg="0";
            } finally {
                if(msg.isEmpty()) {
                    msg="0";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static ArrayList<String> getMsgByUserId(String id) {
        ArrayList<String> msg = new ArrayList<String>();
        try {
            PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("select body from test.messages where user_id = ?");
            pst.setString(1,id);
            ResultSet prs = pst.executeQuery();
            while (prs.next()) {
                if(!prs.getString("body").equals(""))
                    msg.add(prs.getString("body"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }
    public static void AddPhotoListToDb(ArrayList<vkPhoto.items> it) {
        for (int i=0;i<it.size();i++) {
            try {
                PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("insert into test.photo(id,owner_id,lon,lat,text,Country,city,street,Num) values (?,?,?,?,?,?,?,?,?);");
                pst.setString(1,it.get(i).id);
                pst.setString(2,it.get(i).owner_id);
                pst.setString(3,it.get(i).lon);
                pst.setString(4,it.get(i).lat);
                pst.setString(5,it.get(i).text);
                types.GetData geo = geocode.getAdr(it.get(i).lon + "," + it.get(i).lat);
                pst.setString(6, geo.Country);
                pst.setString(7, geo.City);
                pst.setString(8, geo.Street);
                pst.setString(9, geo.Num);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    public static void AddPeopleListToDb(vkProfile.ProfileJson list) {
        for (int i=0;i<list.response.length;i++) {
            try {
                PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("insert into test.people(id,fname,lname,city) values (?,?,?,?);");
                pst.setString(1,list.response[i].id);
                pst.setString(2,list.response[i].first_name);
                pst.setString(3,list.response[i].last_name);
                pst.setString(4,list.response[i].city.title);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    public static void AddWallInfoToDb(VkWall.result input) {
        for(int i = 0;i<input.items.size();i++) {
            try {
                PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("insert into test.Wall(id,owner_id,text,from_id) values (?,?,?,0)");
                pst.setString(1,input.items.get(i).id);
                pst.setString(2,input.items.get(i).owner_id);
                pst.setString(3,input.items.get(i).text);
                pst.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0;i<input.hist.size();i++) {
            try {
                PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("insert into test.Wall(id,owner_id,from_id,text) values (?,?,?,?)");
                pst.setString(1,input.hist.get(i).id);
                pst.setString(2,input.hist.get(i).owner_id);
                pst.setString(3,input.hist.get(i).from_id);
                pst.setString(4,input.hist.get(i).text);
                pst.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void addSimpleList(diffClass ListClass,String table) {
        for (int i=0;i<ListClass.words.size();i++) {
            try {
                PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("insert into classifier."+table+"(word,Count) values (?,?);");
                pst.setString(1,ListClass.words.get(i).text);
                pst.setString(2, String.valueOf(ListClass.words.get(i).Count));
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    public static void addCurrentUser(diffClass ListClass,String id) {
        for (int i=0;i<ListClass.words.size();i++) {
            try {
                PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("insert into classifier.currentUser(word,Count,owner_id) values (?,?,?);");
                pst.setString(1,ListClass.words.get(i).text);
                pst.setString(2, String.valueOf(ListClass.words.get(i).Count));
                pst.setString(3,id);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
    public static diffClass loadSimpleList(String table) {
        diffClass mass = new diffClass(10000);
        try {
            PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("select * from classifier."+table);
            ResultSet rst = pst.executeQuery();
            while(rst.next()) {
                Сlassifier.types.Word word = new Сlassifier.types.Word(rst.getString("word"));
                word.Count = rst.getInt("count");
                mass.words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mass;
    }
    public static diffClass loadCurrentUser(String id) {
        diffClass mass = new diffClass(10000);
        try {
            PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("select * from `classifier`.`currentUser` where `owner_id`= ?;");
            pst.setString(1,id);
            ResultSet rst = pst.executeQuery();
            while(rst.next()) {
                Сlassifier.types.Word word = new Сlassifier.types.Word(rst.getString("word"));
                word.Count = rst.getInt("count");
                mass.words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mass;
    }
    public static void deluser(String id) {
        try {
            PreparedStatement pst = (PreparedStatement) cnt.prepareStatement("DELETE FROM `classifier`.`currentUser` WHERE  `owner_id`= ?;");
            pst.setString(1,id);
            pst.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
