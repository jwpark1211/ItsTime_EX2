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

    private MemberInfo info; //구글에서 받아온 정보(name,email,password)=>embeddable
    private String nickname; //닉네임
    private int battery = 80; //배터리 [default 값 정해야 함]

    //내가 받은 리뷰 리스트
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Review> myReviewList = new ArrayList<>();

    //내가 보낸 리뷰 리스트
    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL)
    private List<Review> sendReviewList = new ArrayList<>();

    //================[method]================//
    public void modifyBattery(int amount){
        //배터리 값 수정
        battery += amount;
    }

    public void setMyReview(Review review){
        //내가 받은 리뷰 리스트에 저장
        myReviewList.add(review);
    }

    public void setSendReview(Review review){
        //내가 보낸 리뷰 리스트에 저장
        sendReviewList.add(review);
    }
}
