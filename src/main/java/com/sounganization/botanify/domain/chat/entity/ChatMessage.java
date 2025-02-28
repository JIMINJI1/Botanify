package com.sounganization.botanify.domain.chat.entity;

import com.sounganization.botanify.common.entity.Timestamped;
import com.sounganization.botanify.domain.chat.components.ChatMessageConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "idx_chat_message_unique", columnList = "room_id,sender_id,content,created_at", unique = true)
})
public class ChatMessage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column
    private LocalDateTime expirationDate;

    @PrePersist
    public void setExpirationDate() {
        this.expirationDate = LocalDateTime.now().plusDays(ChatMessageConstants.DEFAULT_RETENTION_DAYS);
    }

    @Column(nullable = false)
    @Builder.Default
    private Boolean delivered = false;

    @Version
    private Long version;

    public void markAsDelivered() {
        this.delivered = true;
    }

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
}
