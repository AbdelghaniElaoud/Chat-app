package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_invitation_request")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInvitationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "invitation_request_id")
    private InvitationRequest invitationRequest;

    public UserInvitationRequest(User user, InvitationRequest invitationRequest) {
        this.user = user;
        this.invitationRequest = invitationRequest;
    }

    @Override
    public String toString() {
        return "UserInvitationRequest{" +
                "id=" + id +
                ", invitationRequest=" + invitationRequest +
                '}';
    }
}
