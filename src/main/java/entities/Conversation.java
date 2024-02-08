package entities;


import enums.ConversationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conversation")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long conversationId;

    private String name;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "conversation_type")
    private ConversationType conversationType;

    @OneToMany(mappedBy = "conversation"/*, fetch = FetchType.EAGER*/)
    private List<UserConversation> userConversations;

    @OneToMany(mappedBy = "conversation", fetch = FetchType.EAGER)
    private List<Message> messages;


    public Conversation(String name, LocalDateTime createdAt, ConversationType conversationType, List<Message> messages) {
        this.name = name;
        this.createdAt = createdAt;
        this.conversationType = conversationType;
        this.messages = messages;
    }



    @Override
    public String toString() {
        return "Conversation{" +
                "name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", conversationType=" + conversationType +
                '}';
    }
}
