package ItsTime5;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.MemberInfo;
import ItsTime5.Domain.Member.Review;
import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.Study.StudyInfo;
import ItsTime5.Domain.StudyMember.StudyMember;
import ItsTime5.Service.MemberService;
import ItsTime5.Service.StudyMemberService;
import ItsTime5.Service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final MemberService memberService;
        private final StudyService studyService;
        private final StudyMemberService studyMemberService;

        public void dbInit(){

            System.out.println("== init DATA ==");

            //memberInit
            Member member1 = createMember("박정운", "wjddns@naver.com","1234","잇타정운");
            em.persist(member1);
            Member member2 = createMember("송혜수", "gPtn@gmail.com","1234","잇타혜수");
            em.persist(member2);
            Member member3 = createMember("김의진","dmlwls@naver.com","1234","잇타의진");
            em.persist(member3);
            Member member4 = createMember("박지민", "wlals@gmail.com","1234","잇타지민");
            em.persist(member4);
            Member member5 = createMember("정은정", "dmwsjd@gmail.com","1234","잇타은정");
            em.persist(member5);
            Member member6 = createMember("김사은", "tkdms@naver.com","1234","잇타사은");
            em.persist(member6);

            //reviewInit
            Review review1 = new Review(member2,member1,4);
            memberService.saveReview(review1);
            Review review2 = new Review(member3,member1,5);
            memberService.saveReview(review2);
            Review review3 = new Review(member4,member1,5);
            memberService.saveReview(review3);
            Review review4 = new Review(member5,member1,5);
            memberService.saveReview(review4);
            Review review5 = new Review(member6,member1,4);
            memberService.saveReview(review5);
            Review review6 = new Review(member1,member5,5);
            memberService.saveReview(review6);

            //studyInit
            Study study1 = createStudy("자바스터디","서울","월","대면",
                    "프로그래밍",4,"3개월 하실분");
            em.persist(study1);
            Study study2 = createStudy("C++스터디","수도권","월","비대면",
                    "프로그래밍",6,"성실하신분");
            em.persist(study2);
            Study study3 = createStudy("파이썬스터디","서울","토","대면",
                    "프로그래밍",2,"해커톤 준비해요");
            em.persist(study3);
            Study study4 = createStudy("스타트업","대전","일","비대면",
                    "프로그래밍",9,"스타트업 하실 분 모집합니다. 개발기간은" +
                            " 1년 정도로 잡고 있고 초기 서버 비용은 제가 부담합니다. 최소 2년 이상 개발해본 " +
                            "분들 위주로 지원 부탁드립니다. 제 이메일로 프로젝트 깃허브 주소 보내주시면 감사하겠습니다.");

            em.persist(study4);
            Study study5 = createStudy("배드민턴 치실분","서울","월","대면",
                    "취미",4,"같이 배드민턴 칩시다~~~~~");
            em.persist(study5);

            //QuestionInit
            Question question1 = new Question("당신의 나이는?");
            Question question2 = new Question("당신의 학년은?");
            Question question3 = new Question("어느 학교에 재학 중인가요?");
            Question question4 = new Question("개발 실력은?");
            Question question5 = new Question("깃허브 주소를 알려주세요");
            Question question6 = new Question("지원 동기는?");
            Question question7 = new Question("일정을 알려주세요");

            study1.setQuestion(question1);
            study1.setQuestion(question2);
            study2.setQuestion(question3);
            study2.setQuestion(question4);
            study2.setQuestion(question5);
            study3.setQuestion(question6);
            study4.setQuestion(question7);


            studyService.saveQuestion(question1,question2,question3,
                    question4,question5,question6,question7);

            //StudyMemberInit
            StudyMember studyMember1 = createStudyMember(member1.getId(),study1.getId());
            StudyMember studyMember2 = createStudyMember(member2.getId(),study1.getId());
            StudyMember studyMember3 = createStudyMember(member3.getId(),study1.getId());
            StudyMember studyMember4 = createStudyMember(member4.getId(),study1.getId());
            StudyMember studyMember5 = createStudyMember(member5.getId(),study1.getId());
            StudyMember studyMember6 = createStudyMember(member5.getId(),study2.getId());

            studyMemberService.save(studyMember1);
            studyMemberService.save(studyMember2);
            studyMemberService.save(studyMember3);
            studyMemberService.save(studyMember4);
            studyMemberService.save(studyMember5);
            studyMemberService.save(studyMember6);

            studyMemberService.joinStudy(studyMember1);
            studyMemberService.joinStudy(studyMember2);
            studyMemberService.joinStudy(studyMember3);
            studyMemberService.joinStudy(studyMember6);

        }

        private Member createMember(String name, String email, String password, String nickname){
            Member member = new Member();
            MemberInfo info = new MemberInfo(name,email,password);
            member.setInfo(info);
            member.setNickname(nickname);
            return member;
        }
        private Study createStudy(String title, String region, String dayOfWeek, String isOnline
        , String categories, int personLimit, String content){
            Study study = new Study();
            StudyInfo info = new StudyInfo(title, region, dayOfWeek,
                    isOnline,categories,personLimit,content);
            study.setStudyInfo(info);
            return study;
        }
        private StudyMember createStudyMember(Long memberId, Long studyId){
            StudyMember studyMember = new StudyMember();
            studyMember.setStudy(studyService.findOne(studyId));
            studyMember.setMember(memberService.findOne(memberId));
            return studyMember;
        }
    }
}
