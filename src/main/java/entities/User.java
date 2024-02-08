package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.time.LocalDate;
import java.util.List;

// User class
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    private String name ;
    private String email;
    private String phone;
    private String password;
    private String role;
    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "user")
    private List<UserConversation> userConversations;

    @OneToMany(mappedBy = "user")
    private List<UserInvitationRequest> userInvitationRequests;

    public User(String name, String email, String phone, String password, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    public String getProfile() {
        // Implementation
        return null;
    }

    public void changePassword(String newPassword) {
        // Implementation
    }

    public void updateProfile(LocalDate birthDate, String email) {
        // Implementation
    }

    public List<Conversation> getConversations() {
        // Implementation
        return null;
    }

    public void deleteMessage(int idMessage) {
        // Implementation
    }

    public void acceptInvitation(int idInv) {
        // Implementation
    }

    public void deleteInvitation(int idInv) {
        // Implementation
    }

    public void createGroup(String name) {
        // Implementation
    }

    public void exitGroup(int idGroup) {
        // Implementation
    }

    public boolean removeUserFromGroup(int idUser, int idConversation) {
        // Implementation
        return false;
    }

    public boolean addUserToGroup(int idUser, int idConversation) {
        // Implementation
        return false;
    }

    public void deleteUser(int userId) {
        // Implementation
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\''+
                ", isActive='" + isActive + '\''+
                +'}';
    }
}




