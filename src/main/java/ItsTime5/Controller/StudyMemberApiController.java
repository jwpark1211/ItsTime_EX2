package ItsTime5.Controller;

import ItsTime5.Domain.StudyMember.Answer;
import ItsTime5.Domain.StudyMember.Comment;
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

        for(CreateAnswerDTO answerDTO : request.getAnswers()){
            Answer answer = new Answer(answerDTO.getSequence(),answerDTO.getQuestion(),
                    answerDTO.getAnswer(),studyMember);
            System.out.println("sequence:"+answerDTO.getSequence()+"answer"+answerDTO.getAnswer());
            studyMember.setAnswer(answer);
            studyMemberService.saveAnswer(answer);
        }

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
        private List<CreateAnswerDTO> answers;
    }

    @Data //[1]-(1) Request
    static class CreateAnswerDTO{
        private int sequence;
        private String question;
        private String answer;
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

    /* [4] 특정 유저가 작성한(HOST) 스터디 정보 조회
    @GetMapping("/api/studyMember/study/{id}")
    public Result getHostStudyInfo(@PathVariable("id")Long id){
        Member member = memberService.findOne(id);
        List<Study> studyList =

    }*/

    /* [5] 특정 스터디 유저 댓글 생성 */
    @PostMapping("/api/studyMember/{id}/comment")
    public IdResponse saveComment(@PathVariable("id")Long id,
                                  @RequestBody @Valid CreateCommentRequest request){
        StudyMember studyMember = studyMemberService.findOne(id);
        Comment comment = new Comment(request.getComment(),request.getGroupNum(),request.getLayer(),
                request.getSequence(),studyMember);
      Long returnId = studyMemberService.saveComment(comment);
      return new IdResponse(returnId);
    }
    @Data // [1] Request
    static class CreateCommentRequest{
        @NotNull
        private String comment;
        private int groupNum; //댓글 그룹
        private int layer; //계층
        private int sequence; //순서
    }

    /* [6] 특정 스터디 유저 댓글 수정 */
    @PutMapping("/api/studyMember/{id}/comment")
    public IdResponse modifyComment(@PathVariable("id")Long id,
                                    @RequestBody @Valid ModifyCommentRequest request){
        studyMemberService.modifyComment(id,request.getComment());
        return new IdResponse(id);
    }
    @Data // [1] Request
    static class ModifyCommentRequest{
        @NotNull
        private String comment;
    }

    /* [7] 특정 스터디 유저 댓글 삭제 */
    @DeleteMapping("/api/studyMember/{id}/comment")
    public IdResponse deleteComment(@PathVariable("id") Long id){
        studyMemberService.removeComment(id);
        return new IdResponse(id);
    }
}
