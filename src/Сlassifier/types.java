package Сlassifier;

import db.Mysql;
import vk.FileWorker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Fixit on 16.05.2014.
 */
public class types {
    public static class diffClass {
        public ArrayList<Word> words;
        public int CountUsers;

        public diffClass(int CountUsers) {
            this.CountUsers = CountUsers;
            words = new ArrayList<Word>();
        }
        public int getId(String word) {
            int pos = -1;
            for (int i = 0;i<words.size();i++) {
                if(words.get(i).text.equals(word)) {
                    pos = i;
                    break;
                }
            }
            return pos;
        }
        public void addToList(String word) {
            if(getId(word)!=-1) {
                words.get(getId(word)).inc();
            }
            else {
                Word wordClass = new Word(word);
                words.add(wordClass);
            }
        }
        private void addToList(Word word) {
            if(getId(word.text)!=-1) {
                words.get(getId(word.text)).Count += word.Count;
            }
            else {
                words.add(word);
            }
        }
        public void concatination(diffClass LitleBro) {
            for (int i = 0;i<LitleBro.words.size();i++) {
                //addToList(LitleBro.words.get(i));
                words.add(LitleBro.words.get(i));
                //System.out.println(i+"/"+LitleBro.words.size());
            }
        }
        public int getCount(String word) {
            if(getId(word)!=-1) {
                return words.get(getId(word)).Count;
            }
            else {
                return 0;
            }
        }
        public void printAll() {
            System.out.println("Частоты класса:"+CountUsers+" Cуммарное количество слов:"+words.size());
            for (int i = 0;i<words.size();i++) {
                System.out.println(words.get(i).text+" "+words.get(i).Count);
            }
        }
        public void saveTxt(String Path) {
            String temp = "";
            temp = "Частоты класса:"+CountUsers+" Cуммарное количество слов:"+words.size()+System.lineSeparator();
            for (int i = 0;i<words.size();i++) {
                temp = temp + words.get(i).text+" "+words.get(i).Count + System.lineSeparator();
                System.out.println(i + " /"+words.size()+"[OUTPUT]");
            }
            FileWorker.write(Path,temp);
        }
        public void printCount() {
            System.out.println("Частоты класса:"+CountUsers+" Cуммарное количество слов:"+words.size());
        }
        public void loadTxt(String Path) {
            try {
                BufferedReader in = new BufferedReader(new FileReader( new File(Path).getAbsoluteFile()));
                try {
                    String StrLine;
                    int i = 0;
                    while ((StrLine = in.readLine()) != null) {
                        if(StrLine!="") {
                            String[] parts = StrLine.split(" ");
                            Word word = new Word(parts[0]);
                            word.inc(Integer.parseInt(parts[1]));
                            addToList(word);
                            //words.add(word);
                            System.out.println(i + " /2043333[INPUT]");
                            if(i == 2043331) {
                                System.out.println(parts[1]+" "+parts[0]);
                            }
                            i++;
                        }
                    }
                } finally {
                    in.close();
                }
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
            catch (ArrayIndexOutOfBoundsException f) {
                System.out.println();
            }
        }
        public void loadBD(String table) {
            Mysql sql = new Mysql("root","");
            words = sql.loadSimpleList(table).words;
        }
        public void loadUser(String id) {
            Mysql sql = new Mysql("root","");
            words = sql.loadCurrentUser(id).words;
        }
        public void deluser(String id) {
            Mysql sql = new Mysql("root","");
            sql.deluser(id);
        }
    }
    public static class Word {
        public String text = "";
        public int Count = 0;
        public Word(String word) {
            this.text = word;
            this.Count = 1;
        }
        private void inc () {
            this.Count = this.Count + 1;
        }
        private void inc(int count) {
            this.Count +=count;
        }
    }
}
