package ItsTime5.Controller;

import ItsTime5.Domain.Study.Question;
import ItsTime5.Domain.Study.RecruitStatus;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.Study.StudyInfo;
import ItsTime5.Service.StudyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class StudyApiController {

    private final StudyService studyService;

    /* [1] 스터디 신규 생성 */
    @PostMapping("/api/study")
    public CreateStudyResponse saveStudy(@RequestBody @Valid CreateStudyRequest request) {
        Study study = new Study();
        StudyInfo studyInfo = new StudyInfo(request.getTitle(), request.getRegion(), request.getDayOfWeek(),
                request.getIsOnline(), request.getCategories(), request.getPersonLimit(), request.getContent());
        study.setStudyInfo(studyInfo);
        Long studyId = studyService.save(study,request.getMemberId());

        if(request.getQuestions()!=null) {
            for (QuestionDTO questionDto : request.getQuestions()) {
                Question question = new Question(questionDto.getQuestion());
                studyService.saveQuestion(question);
                study.setQuestion(question);
            }
        }

        return new CreateStudyResponse(studyId);
    }

    @Data
    static class CreateStudyRequest {
        @NotNull
        private Long memberId;
        @NotEmpty
        private String title; //스터디제목
        private String region; //지역
        private String dayOfWeek; //요일
        private String isOnline; //대면비대면여부
        private String categories; //공부카테고리
        private int personLimit; //인원제한
        private String content; //소개줄글
        private List<QuestionDTO> questions;
    }

    @Data
    static class QuestionDTO {
        private String question;
    }

    @Data
    @AllArgsConstructor
    static class CreateStudyResponse {
        private Long StudyId;
    }

    /* [2] 스터디 일괄 조회  => 1:m 페치 조인으로!*/
    @GetMapping("/api/study")
    public Result getAllStudy() {
        List<Study> studyList = studyService.findAllWithQuestion();
        List<StudyDTO> result = studyList.stream()
                .map(m -> new StudyDTO(m))
                .collect(Collectors.toList());
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class StudyDTO {
        private Long studyId;
        private String title; //스터디제목
        private String region; //지역
        private String dayOfWeek; //요일
        private String isOnline; //대면비대면여부
        private String categories; //공부카테고리
        private int personLimit; //인원제한
        private String content; //소개줄글
        private RecruitStatus status; //모집상태
        private LocalDateTime postTime; //게시시간
        private List<QuestionDTO2> questions; //질문들

        public StudyDTO(Study study) {
            studyId = study.getId();
            title = study.getStudyInfo().getTitle();
            region = study.getStudyInfo().getRegion();
            dayOfWeek = study.getStudyInfo().getDayOfWeek();
            isOnline = study.getStudyInfo().getIsOnline();
            categories = study.getStudyInfo().getCategories();
            personLimit = study.getStudyInfo().getPersonLimit();
            content = study.getStudyInfo().getContent();
            status = study.getStatus();
            postTime = study.getPostTime();
            questions = study.getQuestionList().stream()
                    .map(question -> new QuestionDTO2(question))
                    .collect(toList());
        }
    }

    @Data
    static class QuestionDTO2{
        private Long questionId;
        private String question;

        public QuestionDTO2(Question question1){
            questionId = question1.getId();
            question = question1.getQuestion();
        }
    }

    /* [3] 특정 스터디 기본 정보를 조회 */
    @GetMapping("/api/study/{id}")
    public StudyDTO GetOneStudy(@PathVariable("id") Long id){
        Study study = studyService.findOneWithQuestion(id);
        StudyDTO studyDTO = new StudyDTO(study);
        return studyDTO;
    }

    /* [4] 특정 스터디 기본 정보를 수정 */
    // Questions, status 는 따로 처리 => 질문 수정 기능 필요한가?????
    @PutMapping("/api/study/{id}")
    public StudyResponse ModifyStudyInfo(@PathVariable("id") Long id,
                                         @RequestBody @Valid UpdateStudyRequest request ){
        StudyInfo info = new StudyInfo(request.getTitle(),request.getRegion(),request.getDayOfWeek()
        , request.getIsOnline(),request.getCategories(),request.getPersonLimit(),request.getContent());
        studyService.modifyStudyInfo(id, info);

        return new StudyResponse("modify success");
    }

    //그대로 띄워주고 원래 null 값이 아니었던 data들도 같이 받아와야 함... 아님 수정하지머
    @Data
    static class UpdateStudyRequest{
        private String title; //스터디제목
        private String region; //지역
        private String dayOfWeek; //요일
        private String isOnline; //대면비대면여부
        private String categories; //공부카테고리
        private int personLimit; //인원제한
        private String content; //소개줄글
    }

    @Data
    @AllArgsConstructor
    static class StudyResponse{
        private String description;
    }

    /* [5] 특정 스터디 삭제 */
    @DeleteMapping("/api/study/{id}")
    public StudyResponse RemoveStudy(@PathVariable("id") Long id){
        studyService.removeStudy(id);
        return new StudyResponse("delete success");
    }

    /* [6] 특정 스터디 모집 마감 */
    @PostMapping("/api/study/{id}")
    public StudyResponse EndRecruit(@PathVariable("id") Long id){
        studyService.endRecruit(id);
        return new StudyResponse("end recruit success");
    }

    /* [7] 키워드로 스터디 조회*/
    @GetMapping("/api/study/search")
    public Result GetStudyWithKey(@RequestBody SearchStudyRequest request){
        List<Study> studyList = studyService.findStudyListByCondition(request.getDayOfWeek(),
                request.getIsOnline(),request.getCategories());
        List<SearchStudyResponse> result = studyList.stream()
                .map(m -> new SearchStudyResponse(m.getId(),m.getStudyInfo().getIsOnline(),
                        m.getStudyInfo().getCategories(),m.getStudyInfo().getTitle(),
                        m.getStudyInfo().getTitle(),m.getStudyInfo().getPersonLimit()))
                .collect(Collectors.toList());
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class SearchStudyResponse {
        private Long StudyId;
        private String isOnline;
        private String categories;
        private String title;
        private String region;
        private int personLimit;
    }

    @Data
    static class SearchStudyRequest{
        private String dayOfWeek;
        private String isOnline;
        private String categories;
    }

}
