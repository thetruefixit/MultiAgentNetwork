package Agent;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;
import Сlassifier.computing;
import Сlassifier.types;

import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings("serial")
public class Analize extends Agent {
    private types.diffClass simple = new types.diffClass(2460);
    private types.diffClass programmer = new types.diffClass(1900);
    private types.diffClass uniq = new types.diffClass(4360);
    private types.diffClass currentUser = new types.diffClass(1);
    private ObjectMessage<MsgTypes.Message> msgClass = null;
    private MsgTypes.Message message = null;
    private AgentAddress boss = null;
    protected void activate()
    {
        setLogLevel(Level.INFO);
        logger.info("Анализатор запущен");
        requestRole(Society.COMMUNITY,Society.GROUP,Society.ROLEANALIZE);
        logger.info("Получил роль:" + Society.ROLEANALIZE);
        simple.loadBD("simplePeople");
        programmer.loadBD("programmer");
        uniq.loadBD("uniq_learn");
        simple.printCount();
        programmer.printCount();
        uniq.printCount();
        logger.info("Инициализировал обучающую выборку!");

    }

    protected void live()
    {
     while(true){
         while(boss == null) {
             boss = getAgentWithRole(Society.COMMUNITY,Society.GROUP,Society.ROLEBOSS);
         }
        msgClass = (ObjectMessage) waitNextMessage();
         if(msgClass.getSender().getRole() == Society.ROLEBOSS) {
            message = msgClass.getContent();
             if(message.operation == Operations.OPERATION_GET_CLASS) {
                 String id = message.data.toString();
                 currentUser.loadUser(id);
                 currentUser.printCount();
                 double result1 = 0.0,result2 = 0.0;
                 try {
                     result1 = computing.getCoef(simple, currentUser, simple.CountUsers + programmer.CountUsers, uniq.words.size());
                     result2 = computing.getCoef(programmer, currentUser, simple.CountUsers + programmer.CountUsers, uniq.words.size());
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 System.out.println(result1+">"+result2);
                 currentUser.deluser(id);
                 currentUser = new types.diffClass(1);
                 if(result1 > result2) {

                     message = new MsgTypes.Message("["+message.data.toString()+"]определен как простой человек",Operations.OPERATION_GET_CLASS);
                 }
                 else message = new MsgTypes.Message("["+message.data.toString()+"]определен как ИТ-шник",Operations.OPERATION_GET_CLASS);
                 logger.info("Получил от босса ид и обработал его!");
                 logger.info("Результат: "+message.data);
                 sendReply(msgClass,new ObjectMessage<MsgTypes.Message>(message));
             }
         }
     }
    }

    protected void end() {
        if(logger != null)
            logger.info("Все сообщения обработаны. Заканчиваю свою работу..");
        pause(10000);
    }


}

