package ItsTime5.Controller;

import ItsTime5.Domain.StudyMember.Answer;
import ItsTime5.Domain.StudyMember.StudyMember;
import ItsTime5.Service.MemberService;
import ItsTime5.Service.StudyMemberService;
import ItsTime5.Service.StudyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudyMemberApiController {

    private final StudyMemberService studyMemberService;
    private final StudyService studyService;
    private final MemberService memberService;

    /* [1] 스터디 멤버 신규 생성 */
    @PostMapping("/api/studyMember")
    public IdResponse saveMember(@RequestBody @Valid CreateStudyMemberRequest request){
        StudyMember studyMember = new StudyMember(memberService.findOne(request.memberId)
                ,studyService.findOne(request.getStudyId()));
        studyMemberService.save(studyMember);
        return new IdResponse(studyMember.getId());
    }

    @Data // [1] [2] [3] [4] Response
    @AllArgsConstructor
    static class IdResponse{
        private Long id;
    }

    @Data // [1] Request
    static class CreateStudyMemberRequest{
        @NotNull
        private Long memberId;
        @NotNull
        private Long studyId;
    }

    /* [2] 특정 스터디 멤버 지원 수락 */
    @PostMapping("/api/studyMember/{id}")
    public IdResponse joinStudyMember(@PathVariable("id")Long id){
        StudyMember studyMember = studyMemberService.findOne(id);
        studyMemberService.joinStudy(studyMember);
        return new IdResponse(studyMember.getId());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    /* [3] 특정 스터디 멤버 삭제 */
    @DeleteMapping("/api/studyMember/{id}")
    public IdResponse deleteStudyMember(@PathVariable("id")Long id){
        Long returnId = studyMemberService.removeStudyMember(id);
        return new IdResponse(returnId);
    }

    /* [4] 특정 스터디 멤버 지원서 생성 */
    @PostMapping("/api/studyMember/{id}/application")
    public IdResponse saveMemberApplication(@PathVariable("id")Long id, @RequestBody @Valid saveApplicationRequest request){
        StudyMember studyMember = studyMemberService.findOne(id);

        for(CreateAnswerDTO answerDTO : request.getAnswers()){
            Answer answer = new Answer(answerDTO.getSequence(),answerDTO.getQuestion(),
                    answerDTO.getAnswer(),studyMember);
            System.out.println("sequence:"+answerDTO.getSequence()+"answer"+answerDTO.getAnswer());
            studyMember.setAnswer(answer);
            studyMemberService.saveAnswer(answer);
            }

        return new IdResponse(id);
    }

    @Data //[4] Request
    static class saveApplicationRequest{
        private List<CreateAnswerDTO> answers;
    }

    @Data //[4]-(1) Request
    static class CreateAnswerDTO{
        private int sequence;
        private String question;
        private String answer;
    }
}
