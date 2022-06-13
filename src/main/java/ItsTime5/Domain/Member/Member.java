package ItsTime5.Domain.Member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private MemberInfo info;
    private String nickname;
    private int battery = 80;

    //내가 받은 리뷰 리스트
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Review> myReviewList = new ArrayList<>();

    //내가 보낸 리뷰 리스트
    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<Review> sendReviewList = new ArrayList<>();

    //================[method]================//
    public void modifyBattery(int amount){
        battery += amount;
    }

    public void setMyReview(Review review){
        myReviewList.add(review);
    }

    public void setSendReview(Review review){
        sendReviewList.add(review);
    }
}
