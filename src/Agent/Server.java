package Agent;

import RestServer_Jax.ServerClass;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;

import java.util.logging.Level;

/**
 * Created by Fixit on 19.05.2014.
 */
@SuppressWarnings("serial")
public class Server extends Agent {
   // private ObjectMessage<String> msg = null; //Структура сообщения, при общении между агентами.
    private ObjectMessage<MsgTypes.Message> msgClass = null;
    private MsgTypes.Message message = null;
    private AgentAddress boss = null;
    protected void activate()
    {
        setLogLevel(Level.INFO);
        requestRole(Society.COMMUNITY,Society.GROUP,Society.ROLESERVER);
        if(logger!=null){
            logger.info("Получил роль: "+Society.ROLESERVER);
        }
        ServerClass.start();
        logger.info("Сервер запущен");
    }

    protected void live()
    {
        int i = 0;
        while(boss == null) {
            i++;
            logger.info("Пытаюсь найти босса.. Попытка:"+i);
            boss = getAgentWithRole(Society.COMMUNITY,Society.GROUP,Society.ROLEBOSS);//Находим агента-boss
            pause(3000);
        }
        logger.info("Босс найден! Начинаю прием заявок..");
        while(true) {
            String id = waitforNextId();
            logger.info("Получен id:"+id);
            sendIdToBoss(id);
        }
    }

    protected void end() {
        if(logger != null)
            logger.info("Пришел приказ на завершения работы. Заканчиваю свою работу..");
        ServerClass.stop();
        pause(10000);
    }
    private String waitforNextId() {
        while(ServerClass.ids.size() == 0){}
        String result = ServerClass.ids.firstElement();
        ServerClass.ids.remove(ServerClass.ids.firstElement());
        return result;
    }

    private void sendIdToBoss(String id) {

        message = new MsgTypes.Message(id,Operations.OPERATION_SEND_ID);
        msgClass = (ObjectMessage)sendMessageAndWaitForReply(boss,new ObjectMessage<MsgTypes.Message>(message),100000);
        if(msgClass==null) {
            if(logger!=null){
                logger.info("Босс не ответил на запросы... Помешаю id обратно в очередь");
                ServerClass.ids.add(id);
                pause(5000);
            }
        }
        else
        if(logger!=null) {
            message = msgClass.getContent();
            logger.info("["+msgClass.getSender().getRole() + "]: - " + message.data);
        }
    }
}
