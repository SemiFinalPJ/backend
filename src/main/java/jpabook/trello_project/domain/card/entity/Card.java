package jpabook.trello_project.domain.card.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jpabook.trello_project.domain.attachment.entity.Attachment;
import jpabook.trello_project.domain.card.dto.request.CreateCardRequestDto;
import jpabook.trello_project.domain.lists.entity.Lists;
import jpabook.trello_project.domain.manager.entity.Manager;
import jpabook.trello_project.domain.reply.entity.Reply;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;
    @Column(length = 100)
    private String title;
    @Column(length = 100)
    private String info;
    @Column(length = 50)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate due;
    @Version
    private Long version;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lists_id", nullable = false)
    private Lists list;
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Attachment> attachmentList = new ArrayList<>();
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reply> replyList = new ArrayList<>();
    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Manager> managers = new ArrayList<>();
    private Long viewCount;
    private int rnk;

    // lists 집어넣는거 아직 추가 안됨
    public Card(CreateCardRequestDto requestDto, Lists list) {
        this.title = requestDto.getTitle();
        this.info = requestDto.getInfo();
        this.due = requestDto.getDue();
        this.list = list;
        this.attachmentList = new ArrayList<>();
        this.replyList = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.viewCount = 0L;
    }

    public void changeTitle(String title) { this.title = title; }
    public void changeInfo(String info) { this.info = info; }
    public void changeDue(LocalDate due) { this.due = due; }
    public void plusViewCount() {this.viewCount ++;}
    public void changeViewCount(Long viewCount) {
        this.viewCount = viewCount;}
}
