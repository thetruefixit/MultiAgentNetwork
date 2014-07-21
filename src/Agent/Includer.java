package Agent;

import db.Mysql;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;
import vk.VkWall;
import vk.vkPhoto;
import Сlassifier.learn;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Fixit on 31.05.2014.
 */
public class Includer extends Agent {
    private ObjectMessage<MsgTypes.Message> msgClass = null; //Структура сообщения, при общении между агентами.
    private AgentAddress boss = null;  // Переменная, содержащая адрес "Босса"
    private MsgTypes.Message message = null;
    private AgentAddress getter = null;
    @Override
    protected void activate() {
        setLogLevel(Level.INFO);
        createGroupIfAbsent(Society.COMMUNITY,Society.GROUP);
        requestRole(Society.COMMUNITY,Society.GROUP,Society.ROLEINCLUDER);
        logger.info("Получил роль: "+Society.ROLEINCLUDER);
        pause(500);
    }
    @Override
    protected void live() {
        while(getter == null) {
            getter = getAgentWithRole(Society.COMMUNITY,Society.GROUP,Society.ROLEGETTER);
        }
        while (true) {
            msgClass = (ObjectMessage) waitNextMessage();
            if(msgClass!=null) {
                if(msgClass.getSender().getRole().equals(Society.ROLEGETTER)) {
                    message = msgClass.getContent();
                    if(message.operation==Operations.OPERATION_PUT_INTO_DB_PHOTO) {
                        MsgTypes.Message<ArrayList<vkPhoto.items>> msg2 = message;
                        message = new MsgTypes.Message("Получил фото! Спасибо!",Operations.OPERATION_RESPONSE);
                        sendReply(msgClass,new ObjectMessage<MsgTypes.Message>(message));
                        logger.info("Получил данные фото, начинаю добавление в БД");
                        Mysql sql = new Mysql("root","");
                        sql.AddPhotoListToDb(msg2.data);
                        logger.info("ФОТО Добавлены в БД!");

                    }
                    if(message.operation==Operations.OPERATION_PUT_INTO_DB_WALL) {
                        MsgTypes.Message<VkWall.result> msg2 = message;
                        message = new MsgTypes.Message("Получил данные, спасибо!",Operations.OPERATION_RESPONSE);
                        sendReply(msgClass,new ObjectMessage<MsgTypes.Message>(message));
                        logger.info("Получил данные стены от [GETTER], начинаю добавление в БД");
                        Mysql sql = new Mysql("root","");
                        sql.addCurrentUser(learn.WallToWords(msg2.data), msg2.data.owner_id);
                        logger.info("СТЕНА Пользователя Добавлена в БД");
                        message = new MsgTypes.Message(msg2.data.owner_id,Operations.OPERATION_SEND_ID);
                        System.out.println("1@"+msg2.data.owner_id);
                        sendMessage(getter,new ObjectMessage<MsgTypes.Message>(message));
                    }
                }
            }
        }
    }

    @Override
    protected void end() {
        if(logger != null)
            logger.info("Заканчиваю свою работу.");
        pause(10000);
    }
}
