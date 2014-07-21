package Agent;


import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;
import vk.VkWall;
import vk.vkPhoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

@SuppressWarnings("serial")

public class Getter extends Agent {
    private ObjectMessage<MsgTypes.Message> msgClass = null; //Структура сообщения, при общении между агентами.
    private MsgTypes.Message message = null;
    private AgentAddress boss = null;  // Переменная, содержащая адрес "Босса"
    private ArrayList<String> msgList = new ArrayList<String>(); //Расширяемый список для хранения объектов.
    private String token = "-1"; // Токен для работы с API Vk.com
    private AgentAddress includer;
    @Override
    protected void activate() {
        setLogLevel(Level.INFO);
        createGroupIfAbsent(Society.COMMUNITY,Society.GROUP);
        //Получение роли хранилища
        requestRole(Society.COMMUNITY,Society.GROUP,Society.ROLEGETTER);
        logger.info("Получил роль: "+Society.ROLEGETTER);
        pause(500);
    }
    @Override
    protected void live() {
        while((includer==null)&(boss == null)) {
            includer = getAgentWithRole(Society.COMMUNITY, Society.GROUP, Society.ROLEINCLUDER);
            boss = getAgentWithRole(Society.COMMUNITY,Society.GROUP,Society.ROLEBOSS);
        }
        while (true) {
            msgClass = (ObjectMessage) waitNextMessage();
            if (msgClass != null) {
                message = msgClass.getContent();
                if (msgClass.getSender().getRole().equals(Society.ROLEBOSS)) {
                    if(message.operation == Operations.OPERATION_TOKEN_SEND) {
                        token = message.data.toString();
                        message = new MsgTypes.Message("Получил токен! Спасибо!",Operations.OPERATION_RESPONSE);
                        sendReply(msgClass, new ObjectMessage<MsgTypes.Message>(message));
                        logger.info("[I] - Получил token <<"+token+">> от ["+msgClass.getSender().getRole()+"]");
                    }
                    if(message.operation == Operations.OPERATION_GET_INFO_FROM_NETWORK) {
                        String id = message.data.toString();
                        message = new MsgTypes.Message("Начинаю загрузку!",Operations.OPERATION_RESPONSE);
                        sendReply(msgClass,new ObjectMessage<MsgTypes.Message>(message));
                        logger.info("[I] - получил id от "+msgClass.getSender().getRole());
                        AddInformation(id);
                    }
                }
                if(msgClass.getSender().getRole().equals(Society.ROLEINCLUDER)) {
                    MsgTypes.Message<String> msg2 = msgClass.getContent();
                    System.out.println("2@"+msg2.operation);
                    if (message.operation.equals(Operations.OPERATION_SEND_ID)) {
                        String id = msg2.data;
                        msg2 = new MsgTypes.Message(id, Operations.OPERATION_SEND_ID);
                        System.out.println("2@" + id);
                        sendMessage(boss, new ObjectMessage<MsgTypes.Message>(msg2));
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
    private void AddInformation(String id) {
       // sendPhotoList(includer,id);
        sendWallList(includer,id);

    }




    private void sendPhotoList(AgentAddress agent,String id) {
        MsgTypes.Message<ArrayList<vkPhoto.items>> photoList = getPhotoList(id);
        if(logger!=null) {
            logger.info("Получил данные по фото для id"+id);
            logger.info("Начинаю отправку Агенту-Includer");
        }
        msgClass = (ObjectMessage) sendMessageAndWaitForReply(agent,new ObjectMessage<MsgTypes.Message<ArrayList<vkPhoto.items>>>(photoList));
        if(msgClass==null) {
            if(logger!=null)
                logger.info("Нет ответа от БАЗЫ ДАННЫХ!!!!");
        }
        else {
            if(logger!=null) {
                message = msgClass.getContent();
                logger.info("Получен ответ от АГЕНТА-БД <<"+message.data);
            }
        }
    }
    private void sendWallList(AgentAddress agent,String id) {
        MsgTypes.Message<VkWall.result> wallClass = getWallList(id);
        wallClass.data.owner_id = id;
        if(logger!=null) {
            logger.info("Получил данные по cтене для id = "+id);
            logger.info("Начинаю отправку to [Includer]");
        }
        msgClass = (ObjectMessage) sendMessageAndWaitForReply(agent,new ObjectMessage<MsgTypes.Message<VkWall.result>>(wallClass));
        if(msgClass==null) {
            if(logger!=null)
                logger.info("Нет ответа от БАЗЫ ДАННЫХ!!!!");
        }
        else {
            if(logger!=null) {
                message = msgClass.getContent();
                logger.info("["+msgClass.getSender().getRole() + "]: - " + message.data);
            }
        }
    }
    private MsgTypes.Message<VkWall.result> getWallList(String id) {
        MsgTypes.Message<VkWall.result> wall = null;
        try {
            wall = new MsgTypes.Message<VkWall.result>(VkWall.getWallById(id,token),Operations.OPERATION_PUT_INTO_DB_WALL);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("ERROR GET_INFO_WALL");
        }
        return wall;
    }
    private MsgTypes.Message<ArrayList<vkPhoto.items>> getPhotoList(String id) {
        MsgTypes.Message<ArrayList<vkPhoto.items>> photoList = null;
        try {
            photoList = new MsgTypes.Message<ArrayList<vkPhoto.items>>(vkPhoto.getAllPhotoById(id,token),Operations.OPERATION_PUT_INTO_DB_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("ERROR GET_INFO_PHOTO");
        }
        return photoList;
    }
}
