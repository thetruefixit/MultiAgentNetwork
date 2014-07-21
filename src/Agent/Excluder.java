package Agent;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.message.ObjectMessage;

import java.util.logging.Level;

/**
 * Created by Fixit on 30.05.2014.
 */
public class Excluder extends Agent {
    private ObjectMessage<String> msg = null; //Структура сообщения, при общении между агентами.
    private AgentAddress boss = null;  // Переменная, содержащая адрес "Босса"
    @Override
    protected void activate() {
        setLogLevel(Level.INFO);
        createGroupIfAbsent(Society.COMMUNITY,Society.GROUP);
        requestRole(Society.COMMUNITY,Society.GROUP,Society.ROLEEXCLUDER);
        logger.info("Получил роль: "+Society.ROLEEXCLUDER);
        pause(500);
    }
    @Override
    protected void live() {
        while (true) {
            msg = (ObjectMessage) waitNextMessage();
            if(msg!=null) {

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
