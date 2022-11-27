import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;
import java.util.Scanner;

//Server
public class ReceiverP implements MessageListener {

    static QueueReceiver receiver = null;
    static QueueSender sender = null;
    // In session are the methods that are needed
    // Queuesession is for messages that are between sender and receiver and waits
    static QueueSession session = null;
    static Queue qSender = null;
    static Queue qReceiver = null;

    //Objects
    Art salvadorDali = new Art("Salvadori", "Plastik", 10);
    Art picasso = new Art("xxxxxxx", "lula", 10000);

    //Objects
    Museum museum;
    Museum museum2 = new Museum("Monalisa", new Art[]{salvadorDali});
    Museum museum1 = new Museum("Der Schrei", new Art[]{picasso});


    InvokeMessage invoke = new InvokeMessage();


    public static void main(String[] argv) throws Exception {
        //to allow to send object messages
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");



        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

        // create an object context with activemq properties
        Context context = new InitialContext(properties);

        // create connection
        QueueConnectionFactory connFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        QueueConnection conn = connFactory.createQueueConnection();
        session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        //qSender(1) and qReceiver(2) for outgoing and incoming messages
        // look at activemq for queue1 and queue2 and cast them
        qReceiver = (Queue) context.lookup("dynamicQueues/queue2");
        qSender = (Queue) context.lookup("dynamicQueues/queue1");

        // create Sender and receiver with Session and part and Queue part
        sender = session.createSender(qSender);
        receiver = session.createReceiver(qReceiver);

        //Init the MessageListener and set it
        ReceiverP listener = new ReceiverP();
        receiver.setMessageListener(listener);

        // The Server starts here
        conn.start();

        //Able to receive messages until input from keyboard
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()) {
            System.out.println("Connection is closed");
            session.close();
            scanner.close();
            conn.close();
        }
    }

    @Override
    public void onMessage(Message message) { //(2)
        ObjectMessage objectMessage = null;

        //casting message to objectMessage to receive object messages
        objectMessage = (ObjectMessage) message;
        try {
            //casting objectMessage to invokeMessage
            invoke = (InvokeMessage) objectMessage.getObject();

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }

        //select museum 1 or 2
        if (invoke.getMuseum() == 1) {
            museum = museum1;
        } else if (invoke.getMuseum() == 2) {
            museum = museum2;
        }


        //Here I add to museum one or two
        if (invoke.getMethod() == 1) {
            museum.addArt(invoke.getName(), invoke.getArtist(), invoke.getValue());
            invoke.setMsg("Add Art to " + museum.getName());
            try {
                //Creating and sending the object invoke (3)
                ObjectMessage sendingObject = session.createObjectMessage(invoke);
                sender.send(sendingObject);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
        //Getting all names from Art
        else if (invoke.getMethod() == 2) {
            String m = "";
            for (int i = 0; i < museum.getArtObject().length; i++) {
                m = m + museum.getArtObject()[i].getName() + " ";
            }
            //Set String m in msg
            invoke.setMsg(m);

            try {
                //Creating and sending sendingObject
                ObjectMessage sendingObject = session.createObjectMessage(invoke);
                sender.send(sendingObject);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
        //Seaching by name an art and set invokeMessage with the attributes
        else if (invoke.getMethod()==3) {
            Art art= museum.SearchByName(invoke.getName());
            InvokeMessage invokeMessage= new InvokeMessage();
            invokeMessage.setName(art.getName());
            invokeMessage.setArtist(art.getArtist());
            invokeMessage.setValue(art.getValue());
            invokeMessage.setMethod(3);

            try {
                ObjectMessage sendingObject = session.createObjectMessage(invokeMessage);
                sender.send(sendingObject);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        }

    }

    }