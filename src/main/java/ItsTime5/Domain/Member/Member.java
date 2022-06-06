package ItsTime5.Domain.Member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private MemberInfo info;
    private String nickname;
    private int battery = 80;

    //================[method]================//
    public void modifyBattery(int amount){
        battery += amount;
    }

}
