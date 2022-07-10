package ItsTime5.Domain.Study;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
public class StudyInfo {

    private String title; //스터디제목
    private String region; //지역
    private String dayOfWeek; //요일
    private String isOnline; //대면비대면여부
    private String categories; //공부카테고리
    private int personLimit; //인원제한
    private String content; //소개줄글
    private String notification;

    protected StudyInfo() {
    }

    public void modifyAll(String title, String region, String dayOfWeek,
                          String isOnline, String categories, int personLimit, String content,String notification){
        this.title = title;
        this.region = region;
        this.dayOfWeek = dayOfWeek;
        this.isOnline = isOnline;
        this.categories = categories;
        this.personLimit = personLimit;
        this.content = content;
        this.notification = notification;
    }

    public void modifyPersonLimit(int amount){
        personLimit += amount;
    }

}
