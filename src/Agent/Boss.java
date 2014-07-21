package Agent;


import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;

import java.io.IOException;
import java.util.IllegalFormatCodePointException;
import java.util.logging.Level;

@SuppressWarnings("serial")

public class Boss extends Agent {
    private String token = "-1"; //Токен, служебная структура, для доступа к API Vk.com
    private ObjectMessage<MsgTypes.Message> msgClass = null;  // Вид сообщения, передаваемого между агентами.
    private MsgTypes.Message<String> message = null;
    private AgentAddress includer = null;
    private AgentAddress excluder = null;
    private AgentAddress getter = null;
    private AgentAddress analize = null;
    private AgentAddress server = null;
    @Override
    // 1-й этап работы агента. Создание структур для работы системы.
    protected void activate() {
        setLogLevel(Level.INFO);
        createGroupIfAbsent(Society.COMMUNITY,Society.GROUP);
        requestRole(Society.COMMUNITY,Society.GROUP,Society.ROLEBOSS);
        if(logger!=null){
            logger.info("Получил роль: "+Society.ROLEBOSS);
        }
        pause(500);
    }
    @Override
    //2-й этап работы агента. Время жизни. Т.е те методы, которые должен выполнить агент.
    protected void live() {
        SearchForAgents();
        try {
            sendTokenToGetter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            msgClass = (ObjectMessage) waitNextMessage();
            if (msgClass != null) {
                if (msgClass.getSender().getRole().equals(Society.ROLESERVER)) {
                    message = msgClass.getContent();
                    String id = message.data;
                    logger.info("Получил id:" + id + ", начинаю обработку:");
                    message = new MsgTypes.Message("Получил id, начинаю обработку...", Operations.OPERATION_RESPONSE);
                    sendReply(msgClass, new ObjectMessage<MsgTypes.Message>(message));
                    ProcessingId(id);
                }
                if(msgClass.getSender().getRole().equals(Society.ROLEGETTER)) {
                    message = msgClass.getContent();
                    if(message.operation.equals(Operations.OPERATION_SEND_ID)) {
                        String id = message.data.toString();
                        message = new MsgTypes.Message(id, Operations.OPERATION_GET_CLASS);
                        ObjectMessage<MsgTypes.Message> msgClass2 = (ObjectMessage) sendMessageAndWaitForReply(analize, new ObjectMessage<MsgTypes.Message>(message),100000);
                        if (msgClass2 == null) {
                            if (logger != null)
                                logger.info("Анализатор не ответил на запрос..");
                        } else if (logger != null) {
                            message = msgClass2.getContent();
                            logger.info("[" + msgClass2.getSender().getRole() + "]: - " + message.data);
                        }
                    }
                }
            }
        }
    }
    @Override
    //3-й этап работы. Завершение работы. Отчистка памяти от созданных структур.
    protected void end() {
        if(logger != null)
            logger.info("Я заканчиваю свою работу..");
        pause(2000);
    }


    private void sendTokenToGetter() throws IOException {  //Отправка токена в хранилище, для работы с данными социальной сети Vk.com
        //Получаем токен
        try {
            token = "6098a426eeebb9f0f503e8eeb67352060d0721cf6621e8c3f28fd99b5bd9f05d0f9e224419dfe255491bd";
            //token = vk.Token.GetToken();
            if(token=="-1") killAgent(this);
        } catch (IllegalFormatCodePointException e) {
            logger.warning("IO EXCEPTION CAPTURED");
        }
        message = new MsgTypes.Message(token,Operations.OPERATION_TOKEN_SEND);
        msgClass = (ObjectMessage)sendMessageAndWaitForReply(getter,new ObjectMessage<MsgTypes.Message>(message),10000); //Отправляем ему токен и ждем ответа
        if(msgClass==null) {
            if(logger!=null){
                logger.info("Хранилище не ответило на запросы...");
                end();
            }
        }
        else
        if(logger!=null) {
            message = msgClass.getContent();
            logger.info("["+msgClass.getSender().getRole() + "]: - " + message.data);
        }
        msgClass = null;
    }
    private void ProcessingId(String id) {
        message = new MsgTypes.Message(id,Operations.OPERATION_GET_INFO_FROM_NETWORK);
        msgClass = (ObjectMessage) sendMessageAndWaitForReply(getter,new ObjectMessage<MsgTypes.Message>(message));
        if(msgClass==null) {
            if(logger!=null)
                logger.info("Выкачеватель не ответил на запрос..");
        }
        else
            if(logger!=null) {
                message = msgClass.getContent();
                logger.info("["+msgClass.getSender().getRole() + "]: - " + message.data);
            }
    }
    private void SearchForAgents() {
        while((getter==null)){//||(includer==null)||(excluder==null)||(analize==null)){
            getter = getAgentWithRole(Society.COMMUNITY, Society.GROUP, Society.ROLEGETTER);
            includer = getAgentWithRole(Society.COMMUNITY, Society.GROUP, Society.ROLEINCLUDER);
          //  excluder = getAgentWithRole(Society.COMMUNITY, Society.GROUP, Society.ROLEEXCLUDER);
            analize = getAgentWithRole(Society.COMMUNITY, Society.GROUP, Society.ROLEANALIZE);
            server = getAgentWithRole(Society.COMMUNITY, Society.GROUP, Society.ROLESERVER);
        }
        pause(1000);
        if(logger!=null) {
            logger.info("Система полна! Начинаю работу..");
        }
    }
}
