package ItsTime5.Controller;

import ItsTime5.Domain.Member.Member;
import ItsTime5.Domain.Member.MemberInfo;
import ItsTime5.Domain.Member.Review;
import ItsTime5.Service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /* [1] 사용자 신규 생성 */  //
    @PostMapping("/api/member")
    public IdResponse saveMember(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        MemberInfo info = new MemberInfo(request.getName(),request.getEmail());
        member.setInfo(info);
        member.setNickname(request.getNickname());
        member.setProfile(request.getProfile());
        Long id = memberService.join(member);

        return new IdResponse(id);
    }

    /* [2] 사용자 일괄 조회 */ //
    @GetMapping("/api/member")
    public Result getAllMember(){
        List<Member> findMembers = memberService.findAll();
        List<GetMemberSimpleInfoResponse> collect = findMembers.stream()
                .map(m -> new GetMemberSimpleInfoResponse(m.getId(),m.getInfo().getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    /* [3] 특정 사용자 기본 정보 조회 */ //
    @GetMapping("/api/member/{id}")
    public GetMemberInfoResponse getMemberInfo(@PathVariable("id") Long id){
        Member member = memberService.findOne(id);
        return new GetMemberInfoResponse(member.getId(),member.getInfo().getName(),
                member.getInfo().getEmail(),member.getNickname(),member.getBattery(),member.getProfile());
    }

    /* [4] 특정 사용자 닉네임 수정 */ //
    @PutMapping("/api/member/{id}")
    public IdResponse updateMember(@PathVariable("id")Long id,
                                   @RequestBody @Valid UpdateMemberRequest request){
        Long returnId = memberService.modifyNickname(id,request.getNickname());
        return new IdResponse(returnId);
    }

    /*[4]-1 특정 사용자 프로필 수정 */
    @PutMapping("/api/member/profile/{id}")
    public IdResponse updateMemberProfile(@PathVariable("id")Long id,
                                          @RequestBody @Valid UpdateMemberProfileRequest request){
        Long returnId = memberService.modifyProfileImage(id,request.getProfile());
        return new IdResponse(returnId);
    }

    /* [5] 특정 사용자 탈퇴
    @DeleteMapping("/api/member/{id}")
    public IdResponse removeMember(@PathVariable("id")Long id){
        Long returnId = memberService.removeMember(id);
        return new IdResponse(returnId);
    }*/

    /* [6] 특정 사용자 후기 일괄 조회 */ //
    @GetMapping("/api/member/{id}/review")
    public Result getMemberReview(@PathVariable("id")Long id){
        List<Review> reviewList = memberService.findAllReviewWithMember(id);
        List<GetReviewInfoResponse> collect = reviewList.stream()
                .map(r -> new GetReviewInfoResponse(r.getId(),r.getSender().getNickname(),r.getStar(),r.getSender().getId()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    /* [7] 특정 사용자 후기 생성 */ //
    @PostMapping("/api/member/{id}/review")
    public IdResponse saveReview(@PathVariable("id")Long recipientId,
                                 @RequestBody @Valid CreateReviewRequest request){
        Member recipient = memberService.findOne(recipientId);
        Member sender = memberService.findOne(request.senderId);
        Review review = new Review(sender,recipient,request.getStar());
        Long reviewId = memberService.saveReview(review);

        return new IdResponse(reviewId);
    }

    /*================================<<DTO>>====================================*/

    @Data // 데이터 list 형태로 반환할 때
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data // [1] Request
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
        private String email;
        @NotEmpty
        private String nickname;
        private int profile;
    }

    @Data // [4] Request
    static class UpdateMemberRequest{
        private String nickname;
        //+)이미지 정보도 나중에 한꺼번에 저장 및 관리
    }

    @Data
    static class UpdateMemberProfileRequest{
        private int profile;
    }

    @Data //[7] Request
    static class CreateReviewRequest{
        private Long senderId;
        private int star;
    }

    @Data // [1] [4] [5] [7] Response
    @AllArgsConstructor
    static class IdResponse{
        private Long id;
    }

    @Data // [2] Response
    @AllArgsConstructor
    static class GetMemberSimpleInfoResponse{
        private Long id;
        private String name;
    }

    @Data // [3] Response
    @AllArgsConstructor
    static class GetMemberInfoResponse{
        private Long id;
        private String name;
        private String email;
        private String nickname;
        private int battery;
        private int profile;
    }

    @Data // [6] Response
    @AllArgsConstructor
    static class GetReviewInfoResponse{
        private Long id;
        private String nickname;
        private int star;
        private Long sender_id;
    }
}
