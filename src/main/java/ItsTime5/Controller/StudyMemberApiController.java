package ItsTime5.Controller;

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

@RestController
@RequiredArgsConstructor
public class StudyMemberApiController {

    private final StudyMemberService studyMemberService;
    private final StudyService studyService;
    private final MemberService memberService;

    /* [1] 스터디 멤버 신규 생성 */
    @PostMapping("/api/studyMember")
    public StudyMemberResponse saveMember(@RequestBody @Valid CreateStudyMemberRequest request){
        StudyMember studyMember = new StudyMember();
        studyMember.setMember(memberService.findOne(request.memberId));
        studyMember.setStudy(studyService.findOne(request.studyId));
        studyMemberService.save(studyMember);
        return new StudyMemberResponse(studyMember.getId());
    }

    @Data
    @AllArgsConstructor
    static class StudyMemberResponse{
        private Long id;
    }

    @Data
    static class CreateStudyMemberRequest{
        @NotNull
        private Long memberId;
        @NotNull
        private Long studyId;
    }

    /* [2] 특정 스터디 멤버 지원 수락 */
    @PostMapping("/api/studyMember/{id}")
    public StudyMemberResponse joinStudyMember(@PathVariable("id")Long id){
        StudyMember studyMember = studyMemberService.findOne(id);
        studyMemberService.joinStudy(studyMember);
        return new StudyMemberResponse(studyMember.getId());
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    /* [3] 특정 스터디 멤버 삭제 */
    @DeleteMapping("/api/studyMember/{id}")
    public StudyMemberResponse deleteStudyMember(@PathVariable("id")Long id){
        Long returnId = studyMemberService.removeStudyMember(id);
        return new StudyMemberResponse(returnId);
    }
}
