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

    /* [1] 사용자 신규 생성 */
    @PostMapping("/api/member")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        MemberInfo info = new MemberInfo(request.getName(),request.getEmail(),request.getPassword());
        member.setInfo(info);
        member.setNickname(request.nickname);
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
        private String email;
        private String password;

        @NotEmpty
        private String nickname;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse{
        private Long id;
    }

    /* [2] 사용자 일괄 조회 */
    @GetMapping("/api/member")
    public Result getAllMember(){
        List<Member> findMembers = memberService.findAll();

        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getId(),m.getInfo().getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private Long id;
        private String name;
    }

    /* [3] 특정 사용자 기본 정보 조회 */
    @GetMapping("/api/member/{id}")
    public GetMemberResponse getMemberInfo(@PathVariable("id") Long id){
        Member member = memberService.findOne(id);
        return new GetMemberResponse(member.getId(),member.getInfo().getName(),
                member.getInfo().getEmail(),member.getNickname(),member.getBattery());
    }

    @Data
    @AllArgsConstructor
    static class GetMemberResponse{
        private Long id;
        private String name;
        private String email;
        private String nickname;
        private int battery;
    }

    /* [4] 특정 사용자 기본 정보 수정 */
    @PutMapping("/api/member/{id}")
    public MemberResponse updateMember(@PathVariable("id")Long id,
                                             @RequestBody @Valid UpdateMemberRequest request){
        memberService.modifyNickname(id,request.getNickname());
        return new MemberResponse("modify success");
    }

    @Data
    static class UpdateMemberRequest{
        private String nickname;
        //+)이미지 정보도 나중에 한꺼번에 저장 및 관리
    }

    @Data
    @AllArgsConstructor
    static class MemberResponse{
        private String description;
    }

    /* [5] 특정 사용자 탈퇴 */
    @DeleteMapping("/api/member/{id}")
    public MemberResponse removeMember(@PathVariable("id")Long id){
        memberService.removeMember(id);
        return new MemberResponse("delete success");
    }

    /* [6] 특정 사용자 후기 일괄 조회 */
    @GetMapping("/api/member/{id}/review")
    public Result getMemberReview(@PathVariable("id")Long id){
       List<Review> reviewList = memberService.findAllReviewWithMember(id);
       List<ReviewDto> collect = reviewList.stream()
               .map(r -> new ReviewDto(r.getId(),r.getSender().getNickname(),r.getStar(),r.getSender().getId()))
               .collect(Collectors.toList());
       return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class ReviewDto{
        private Long id;
        private String nickname;
        private int star;
        private Long sender_id;
    }

    /* [7] 특정 사용자 후기 생성 */
    @PostMapping("/api/member/{id}/review")
    public CreateReviewResponse saveReview(@PathVariable("id")Long recipientId,@RequestBody @Valid CreateReviewRequest request){
        Member recipient = memberService.findOne(recipientId);
        Member sender = memberService.findOne(request.senderId);
        Review review = new Review(sender,recipient,request.getStar());
        recipient.setMyReview(review);
        sender.setSendReview(review);
        Long reviewId = memberService.saveReview(review);
        return new CreateReviewResponse(reviewId);
    }

    @Data
    static class CreateReviewRequest{
        private Long senderId;
        private int star;
    }
    @Data
    @AllArgsConstructor
    static class CreateReviewResponse{
        private Long id;
    }
}
