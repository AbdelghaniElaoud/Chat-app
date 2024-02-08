import dao.UserDao;
import dao.UserDaoImpl;
import entities.User;

import java.util.List;

public class Main {
    public static void main(String[] args)  {


        /*Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        Conversation conversation = session.find(Conversation.class, 4L);
        for (Message message: conversation.getMessages()){
            System.out.println(message.getTextType());
        }


        session.getTransaction().commit();*/



        UserDaoImpl userDao = new UserDaoImpl();

//        List<User> users = userDao.getUsersOfConversationByConversationName("jon bones");
//
//        for (User user:users){
//            System.out.println(user);
//        }

        /*List<Message> messages = userDao.getMessagesOfConversation(4L);

        for (Message message : messages){
            System.out.println(message.getTextType());
        }*/

        /*userDao.sendMessage(2L, 4L, "Fine for the moment :)",null);*/
        /*for (Conversation conversation : userDao.getUserConversations(1L)){
            System.out.println(conversation);
        }*/

        List<User> users = userDao.getAllUsersWithoutAdmins();

        for (User user:users){
            System.out.println(user);
        }

        /*Session session = HibernateUtil.getSessionFactory().getCurrentSession();



        session.getTransaction().begin();

        User user = session.find(User.class, 1L);

        UserConversation userConversation = new UserConversation(user, false);

        session.persist(userConversation);

        List<UserConversation> userConversationList = new ArrayList<>();
        userConversationList.add(userConversation);

        Conversation conversation = new Conversation(user.getName(), LocalDateTime.now(), ConversationType.NORMAL, userConversationList, null);

        session.persist(conversation);

        session.getTransaction().commit();*/







    }
}
