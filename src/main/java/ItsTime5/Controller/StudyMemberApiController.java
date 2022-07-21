package ItsTime5.Controller;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Study.Study;
import ItsTime5.Domain.StudyMember.*;
import ItsTime5.Service.MemberService;
import ItsTime5.Service.StudyMemberService;
import ItsTime5.Service.StudyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Host;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class StudyMemberApiController {

    private final StudyMemberService studyMemberService;
    private final StudyService studyService;
    private final MemberService memberService;

    /* [1] 스터디 멤버 신규 생성 */ //
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

    /* [2] 특정 스터디 멤버 지원 수락 */ //
    @PostMapping("/api/studyMember/{id}")
    public IdResponse joinStudyMember(@PathVariable("id")Long id){
        Long studyMemberId = studyMemberService.joinStudy(id);
        return new IdResponse(studyMemberId);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    /* [3] 특정 스터디 멤버 삭제
    @DeleteMapping("/api/studyMember/{id}")
    public IdResponse deleteStudyMember(@PathVariable("id")Long id){
        Long returnId = studyMemberService.removeStudyMember(id);
        return new IdResponse(returnId);
    }*/

    /* [4] 특정 유저가 속한 스터디 정보 조회*/ //
    @GetMapping("/api/studyMember/study/{id}")
    public Result getStudyInfoWithMemberId(@PathVariable("id")Long id){
        List<Study> studyList = studyMemberService.findAllStudyWithMemberId(id);
        List<getStudyInfoWithMemberIdResponse> result = studyList.stream()
                .map(m -> new getStudyInfoWithMemberIdResponse(
                        m.getStudyInfo().getRegion(),m.getStudyInfo().getIsOnline(),
                        m.getStudyInfo().getTitle(),m.getPostTime(),m.getId()))
                .collect(Collectors.toList());
        return new Result(result);
    }

    /* [4]-1 특정 유저가 작성한 스터디 정보 조회 */ //
    @GetMapping("/api/studyMember/study/Host/{id}")
    public Result getStudyInfoByHostWithMemberId(@PathVariable("id")Long id){
        List<Study> studyList = studyMemberService.findAllStudyByHostWithMemberId(id);
        List<getStudyInfoWithMemberIdResponse> result = studyList.stream()
                .map(m -> new getStudyInfoWithMemberIdResponse(
                        m.getStudyInfo().getRegion(),m.getStudyInfo().getIsOnline(),
                        m.getStudyInfo().getTitle(),m.getPostTime(),m.getId()))
                .collect(Collectors.toList());
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class getStudyInfoWithMemberIdResponse{
        private String region;
        private String isOnline;
        private String title;
        private LocalDateTime postTime;
        private Long studyId;
    }

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

    /* [6] 특정 스터디 유저 댓글 수정 */ //
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

    /* [8] 특정 스터디의 스터디 멤버 정보 전체 조회 */ //
    @GetMapping("/api/studyMember/{id}/study")
    public Result getMemberInfoWithStudyId(@PathVariable("id") Long id){
        Member member = studyMemberService.findHostMemberWithStudyId(id);
        getMemberInfoWithStudyIdResponse HostResponse =
                new getMemberInfoWithStudyIdResponse(member.getNickname(),MemberGrade.host,
                        member.getBattery(),member.getProfile(), member.getId());
        List<Member> memberList = studyMemberService.findMemberWithStudyId(id);
        for (Member member1 : memberList) {
            System.out.println(member.getNickname());
        }
        List<getMemberInfoWithStudyIdResponse> result = memberList.stream()
                .map(m->new getMemberInfoWithStudyIdResponse(m.getNickname(),MemberGrade.guest,
                        m.getBattery(),m.getProfile(),m.getId()))
                .collect(Collectors.toList());
        result.add(HostResponse);
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class getMemberInfoWithStudyIdResponse{
        private String nickname;
        private MemberGrade grade;
        private int battery;
        private int profile;
        private Long memberId;
    }
}
