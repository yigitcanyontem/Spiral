package com.yigitcanyontem.forum.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "socialmedia",
        uniqueConstraints = {
        @UniqueConstraint(
        name = "user_usersid_unique",
        columnNames = "usersid")
        }
)
@NoArgsConstructor
public class SocialMedia {
    @Id
    @SequenceGenerator(
            name = "socialmedia_id_seq",
            sequenceName = "socialmedia_id_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "socialmedia_id_seq"
    )
    private Integer id;
    @OneToOne()
    @JoinColumn(
            name = "usersid",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Users usersid;
    @Column(name = "instagram")
    private String instagram;
    @Column(name = "pinterest")
    private String pinterest;
    @Column(name = "linkedin")
    private String linkedin;
    @Column(name = "twitter")
    private String twitter;

    public SocialMedia(Users usersid, String instagram, String pinterest, String linkedin, String twitter) {
        this.usersid = usersid;
        this.instagram = instagram;
        this.pinterest = pinterest;
        this.linkedin = linkedin;
        this.twitter = twitter;
    }

    public SocialMedia(Integer id, Users usersid, String instagram, String pinterest, String linkedin, String twitter) {
        this.id = id;
        this.usersid = usersid;
        this.instagram = instagram;
        this.pinterest = pinterest;
        this.linkedin = linkedin;
        this.twitter = twitter;
    }

    @Override
    public String toString() {
        return "SocialMedia{" +
                "id=" + id +
                ", usersid=" + usersid +
                ", instagram='" + instagram + '\'' +
                ", pinterest='" + pinterest + '\'' +
                ", linkedin='" + linkedin + '\'' +
                ", twitter='" + twitter + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUsersid() {
        return usersid;
    }

    public void setUsersid(Users usersid) {
        this.usersid = usersid;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
