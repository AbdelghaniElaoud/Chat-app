package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_conversation")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    @Column(name = "is_admin_group")
    private boolean isAdminGroup;

    public UserConversation(User user,Conversation conversation, boolean isAdminGroup) {
        this.user = user;
        this.isAdminGroup = isAdminGroup;
        this.conversation = conversation;
    }

    @Override
    public String toString() {
        return "UserConversation{" +
                "user=" + user +
                ", isAdminGroup=" + isAdminGroup +
                '}';
    }
}