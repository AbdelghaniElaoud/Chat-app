package dao;

import entities.*;

import java.util.List;

public interface UserDao {

    User getUserById(Long userId);
    String getUsernameById(Long userId);

    List<User> getUsersOfConversationByConversationName(String conversationName);
    void sendInvitation(Long senderId, Long receiverId);

    List<UserInvitationRequest> checkInvitations(Long receiverId);

    void acceptOrDeclineAnInvitation(Long invitationId, boolean value);

    List<Conversation> getUserConversations(Long idUser);

    void createConversation(User userAccepting, User userAccepted);
    void sendMessage(Long senderId, Long conversationId, String textContent, byte[] blobContent);
    List<Message>  getMessagesOfConversation(Long idConversation);

    Conversation getConversationByNameAndUser(String conversationName, Long userId);
    boolean updateProfile(Long userId, String email, String phone);
    boolean updatePassword(Long userId, String password);
    List<User> getAllUsersWithoutAdmins();
    User activateOrDeactivateUser(Long userId);
    User createUser(String name, String email, String phoneNumber, String s, String password);

    boolean exists(String mail);

    List<User> getTheNonFriendsOfUserById(Long idUser);

    List<UserConversation> getUserConversationOfConversationById(Long userId);

    Long getUserIdByName(String name);
    void createGroupChat(Long adminId, String groupChatName, List<Long> userConversation);
}
