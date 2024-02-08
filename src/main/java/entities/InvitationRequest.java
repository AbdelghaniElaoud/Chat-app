package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation_request")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InvitationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invitation_request")
    private Long idInvitationRequest;
    @Column(name = "sender_id")
    private Long senderId;
    @Column(name = "receiver_id")
    private Long receiverId;
    @Column(name = "accepted")
    private boolean accepted;
    @Column(name = "sent_time")
    private LocalDateTime sentTime;
    @Column(name = "accepted_time")
    private LocalDateTime acceptedTime;

    public InvitationRequest(Long senderId, Long receiverId, LocalDateTime sentTime, User user) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.accepted = false;
        this.sentTime = sentTime;
        this.acceptedTime = null;
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Override
    public String toString() {
        return "InvitationRequest{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", accepted=" + accepted +
                ", sentTime=" + sentTime +
                ", acceptedTime=" + acceptedTime +
                '}';
    }
}