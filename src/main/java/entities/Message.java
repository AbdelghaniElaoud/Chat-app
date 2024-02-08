package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

// Message class
@Entity
@Table(name = "message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "text_type")
    private String textType;
    @Column(name = "blob_type")
    private byte[] blobType;
    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;


    public Message(Long senderId, String textType, byte[] blobType, LocalDateTime sentTime, Conversation conversation) {
        this.senderId = senderId;
        this.textType = textType;
        this.blobType = blobType;
        this.sentTime = sentTime;
        this.conversation = conversation;
    }
}