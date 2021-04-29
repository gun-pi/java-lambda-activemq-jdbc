import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
/*import org.example.untitled.db.DocumentDao;
import org.example.untitled.db.DocumentEntity;*/
import org.example.untitled.db.DocumentDao;
import org.example.untitled.db.DocumentEntity;
import org.example.untitled.mq.Mq;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import java.sql.SQLException;

public class HandlerTest {

    @Test
    void test() {
        /*String input = "test";
        String message = null;
        try {
            Mq.sendMessage(pooledConnectionFactory, input);
            message = Mq.receiveMessage(connectionFactory);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        pooledConnectionFactory.stop();

        System.out.println(message);

        Long id = null;
        try {
            id = documentDao.save(new DocumentEntity(message));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(id + ": here I am");*/
    }
}
