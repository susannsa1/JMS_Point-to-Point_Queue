import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;
import java.util.Scanner;

//Client
public class SenderP {
    public static void main(String[] argv) throws Exception {

        // For JMS SenderP and ReceiverP accepting ObjectMessages
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

        System.out.println("Client!\n");

        Hashtable<String, String> properties = new Hashtable<String, String>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

        Context context = new InitialContext(properties);

        QueueConnectionFactory connFactory =
                (QueueConnectionFactory) context.lookup("ConnectionFactory");

        QueueConnection conn = connFactory.createQueueConnection();
        QueueSession session = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        //qReceiver(1) and qSender(2) for outgoing and incoming messages
        Queue qReceiver = (Queue) context.lookup("dynamicQueues/queue1");
        Queue qSender = (Queue) context.lookup("dynamicQueues/queue2");

        //Create for qSender ObjectMessage
        QueueSender sender = session.createSender(qSender);
        QueueReceiver receiver = session.createReceiver(qReceiver);


        //Create for qReceiver ObjectMessage
        conn.start();
        while (true) {
             InvokeMessage message = new InvokeMessage();
             InvokeMessage message2 = null;
            // Scanner erstellt
            Scanner scanner = new Scanner(System.in);


            System.out.println("Which museum do you want select 1 or 2: ");
            String selectmuseum = scanner.nextLine();
            message.setMuseum(Integer.parseInt(selectmuseum));

            System.out.println("For Adding press 1: ");
            System.out.println("For Getting all the art names from museum press 2:  ");
            System.out.println("For Searching an art press 3: ");
            String select = scanner.nextLine();

            //For adding
            if (select.equals("1")) {
                message.setMethod(1);
                System.out.println("Name of Art: ");
                String nameofart = scanner.nextLine();
                message.setName(nameofart);

                System.out.println("Name of Artist: ");
                String artist = scanner.nextLine();
                message.setArtist(artist);

                System.out.println("The Value: ");
                String value = scanner.nextLine();
                message.setValue(Integer.parseInt(value));


            } //getting all the names of arts from museum
            else if (select.equals("2")) {
                message.setMethod(2);

            } //Searching an art
            else if (select.equals("3")) {
                message.setMethod(3);
                System.out.println("Which art do you want: ");
                String nameofart2 = scanner.nextLine();
                message.setName(nameofart2);
            }

            //object in eine objectmesssage packen
            ObjectMessage sendObject= session.createObjectMessage(message);
            System.out.println("Sending Object: "+sendObject.getObject());
            //(1)
            sender.send(sendObject);


            // Creating receive and receive object (4)
            ObjectMessage receivedObject = session.createObjectMessage();
            receivedObject=(ObjectMessage) receiver.receive();

            try {
                //Casting receivedObject to invokMessage
                //Print msg from message2 out
                message2 = (InvokeMessage) receivedObject.getObject();
                if (message2.getMethod()==1 | (message.getMethod()==2)){
                    System.out.println("Reply: "+message2.getMsg());
                }
                //Print the attributes from message2
                else if (message2.getMethod()==3) {
                    System.out.println("Reply: "+message2.getName() +
                            "\nArtist: "+message2.getArtist() +
                            "\nValue: "+message2.getValue());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }


            //System.out.println("hallo");
            //session.close();
            //conn.close();


        }
    }
}
