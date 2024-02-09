package dao;

import entities.*;
import enums.ConversationType;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.time.LocalDateTime;
import java.util.*;


@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    /*Session session;

    public UserDaoImpl() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
    }*/

    @Override
    public User getUserById(Long userId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();

        User user = session.find(User.class, userId);


        session.getTransaction().commit();

        return user;
    }

    @Override
    public String getUsernameById(Long userId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.getTransaction().begin();
        String hql = "SELECT u.name FROM User u WHERE u.userId = :userId";

        String userName = session.createQuery(hql, String.class)
                .setParameter("userId", userId)
                .uniqueResult();


        session.getTransaction().commit();

        return userName;
    }

    @Override
    public List<User> getUsersOfConversationByConversationName(String conversationName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        String hql = "SELECT DISTINCT uc.user FROM UserConversation uc " +
                "JOIN uc.conversation c " +
                "WHERE c.name = :conversationName";

        List<User> userList = session.createQuery(hql, User.class)
                .setParameter("conversationName", conversationName)
                .getResultList();



        session.getTransaction().commit();


        return userList;
    }

    /**
     * Send invitation request to other users
     *
     * @param receiverId
     */
    @Override
    public void sendInvitation(Long senderId, Long receiverId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        try {

            session.getTransaction().begin();

            //1
            User userSender = session.find(User.class, senderId);
            User userReceiver = session.find(User.class, receiverId);

            if (userSender == null) {
                throw new IllegalArgumentException("Sender with ID " + senderId + " not found");
            }

            InvitationRequest invitationRequest = new InvitationRequest(userSender.getUserId(), receiverId, LocalDateTime.now(), userReceiver);
            session.persist(invitationRequest);

            //2


            if (userReceiver == null) {
                throw new IllegalArgumentException("Receiver with ID " + receiverId + " not found");
            }

            UserInvitationRequest userInvitationRequest = new UserInvitationRequest(userReceiver, invitationRequest);
            session.persist(userInvitationRequest);

            session.getTransaction().commit();

        } catch (Exception e) {
            // Handle any exception that might occur during the process
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Replace with appropriate logging mechanism
        }
    }

    @Override
    public List<UserInvitationRequest> checkInvitations(Long receiverId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        List<UserInvitationRequest> invitations = new ArrayList<>();

        try {
            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }

            if (receiverId != null) {
                User user = session.find(User.class, receiverId);

                // Assuming UserInvitationRequest is a property of User, adjust accordingly
                if (user != null && user.getUserInvitationRequests() != null) {
                    invitations.addAll(user.getUserInvitationRequests());
                }
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            // Handle exceptions appropriately, for example, log the exception
            e.printStackTrace();

            // Rollback the transaction in case of an exception
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }

        return invitations;
    }

    @Override
    public void acceptOrDeclineAnInvitation(Long invitationId, boolean value) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        //Get the invitation
        InvitationRequest invitationRequest = session.find(InvitationRequest.class, invitationId);

        if (invitationRequest != null && value) {
            invitationRequest.setAccepted(true);
            invitationRequest.setAcceptedTime(LocalDateTime.now());
        } else {
            session.remove(invitationRequest);
        }


        session.getTransaction().commit();
    }

    @Override
    public List<Conversation> getUserConversations(Long idUser) {

        List<Conversation> conversations = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        try {
            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }

            // Assuming you have a mapped association between User and UserConversation entities
            Query<Conversation> query = session.createQuery(
                    "SELECT uc.conversation FROM UserConversation uc WHERE uc.user.userId = :userId",
                    Conversation.class
            );
            query.setParameter("userId", idUser);
            conversations = query.getResultList();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace(); // Handle or log the exception appropriately
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        return conversations;
    }


    public List<UserInvitationRequest> getTheAcceptedInvitations(Long userId){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }
        List<UserInvitationRequest> invitationRequests = checkInvitations(userId);

        List<UserInvitationRequest> copy = new ArrayList<>();

        for (UserInvitationRequest invitationRequest : invitationRequests){
            if (invitationRequest.getInvitationRequest().isAccepted()){
                copy.add(invitationRequest);
            }
        }

        return copy;
    }


    @Override
    public void createConversation(User userAccepting, User userAccepted) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        if (!session.getTransaction().isActive()){
            session.getTransaction().begin();
        }

        Conversation conversation = new Conversation(userAccepted.getName(), LocalDateTime.now(), ConversationType.NORMAL, null);
        session.persist(conversation);

        UserConversation userConversation1 = new UserConversation(userAccepting,conversation, false);
        UserConversation userConversation2 = new UserConversation(userAccepted,conversation, false);



        session.persist(userConversation1);
        session.persist(userConversation2);

        sendMessage(userAccepting.getUserId(), conversation.getConversationId(), "Getting Started",null);


//        session.getTransaction().commit();

    }

    @Override
    public void sendMessage(Long senderId, Long conversationId, String textContent, byte[] blobContent) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try {
            if (!session.getTransaction().isActive()){
                transaction = session.beginTransaction();
            }

            if (conversationId != null && senderId != null) {
                Conversation conversation = session.find(Conversation.class, conversationId);
                Message message = new Message(senderId, textContent, blobContent, LocalDateTime.now(), conversation);

                session.persist(message);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Message> getMessagesOfConversation(Long idConversation) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.beginTransaction();
        String hql = "SELECT m FROM Message m JOIN m.conversation c " +
                "WHERE c.conversationId = :conversationId";

        List<Message> messages = session.createQuery(hql, Message.class)
                .setParameter("conversationId", idConversation)
                .getResultList();


        session.getTransaction().commit();

        return messages;
    }

    @Override
    public Conversation getConversationByNameAndUser(String conversationName, Long userId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        String hql = "SELECT c FROM Conversation c " +
                "JOIN FETCH c.messages " +
                "JOIN c.userConversations uc " +
                "WHERE c.name = :conversationName " +
                "AND uc.user.id = :userId";

        Conversation conversation = session.createQuery(hql, Conversation.class)
                .setParameter("conversationName", conversationName)
                .setParameter("userId", userId)
                .uniqueResult();


        session.getTransaction().commit();

        return conversation;
    }

    @Override
    public boolean updateProfile(Long userId, String email, String phone) {
        if (userId != null && email != null && phone !=null){
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();

            session.getTransaction().begin();

            User user = session.find(User.class, userId);

            user.setEmail(email);
            user.setPhone(phone);

            session.persist(user);

            session.getTransaction().commit();
            return true;
        }

        return false;
    }

    @Override
    public boolean updatePassword(Long userId,String password) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        User user = session.find(User.class, userId);

        user.setPassword(password);

        session.persist(user);

        session.getTransaction().commit();
        return true;
    }

    @Override
    public List<User> getAllUsersWithoutAdmins() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        String hql = "FROM User WHERE role <> 'admin'";
        Query query = session.createQuery(hql);
        List<User> userList = query.list();


        session.getTransaction().commit();


        return userList;
    }

    @Override
    public User activateOrDeactivateUser(Long userId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        User user = session.find(User.class,userId);

        user.setActive(!user.isActive());

        session.persist(user);

        session.getTransaction().commit();
        return user;
    }

    /**
     * @param name
     * @param email
     * @param phoneNumber
     * @param role
     * @param password
     * @return user
     * : This method store the user into the database
     */
    @Override
    public User createUser(String name, String email, String phoneNumber, String role, String password) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        User user = null;

        try {
            session.beginTransaction();

            User userToSave = new User(name, email, phoneNumber, password, role);
            userToSave.setActive(Objects.equals(role, "admin"));
            session.persist(userToSave);


            Long userId = userToSave.getUserId();

            if (userId != null) {
                user = userToSave;
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
        }

        return user;
    }

    @Override
    public boolean exists(String mail) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        Query query = session.
                createQuery("select 1 from User u where u.email = :email");
        query.setString("email", mail );

        var result = query.uniqueResult();

        session.getTransaction().commit();

        return result != null;
    }

    @Override
    public List<User> getTheNonFriendsOfUserById(Long idUser) {

        List<Conversation> conversations = getUserConversations(idUser);
        List<User> users = getAllUsersWithoutAdmins();

        Set<Long> userConversationsSet = new HashSet<>();

        for (Conversation conversation : conversations){
            List<UserConversation> userConversations = getUserConversationOfConversationById(conversation.getConversationId());
             for (UserConversation userConversation : userConversations){
                  userConversationsSet.add(userConversation.getUser().getUserId());
             }
        }

        List<User> nonFriends = new ArrayList<>();

        for (User user:users){
            if (!userConversationsSet.contains(user.getUserId())){
                nonFriends.add(user);
            }
        }

        nonFriends.remove(idUser);


        return nonFriends;
    }

    @Override
    public List<UserConversation> getUserConversationOfConversationById(Long conversationId) {


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        String hql = "SELECT uc FROM UserConversation uc " +
                "WHERE uc.conversation.conversationId = :specificConversationId";

        List<UserConversation> userConversations = session.createQuery(hql, UserConversation.class)
                .setParameter("specificConversationId", conversationId)
                .getResultList();

        session.getTransaction().commit();
        return userConversations;

    }

    @Override
    public Long getUserIdByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();

        session.getTransaction().begin();

        String hql = "SELECT u.userId FROM User u WHERE u.name = :userName";

        Long userId = session.createQuery(hql, Long.class)
                .setParameter("userName", name)
                .getSingleResult();



        session.getTransaction().commit();


        return userId;
    }


}
