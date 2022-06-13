package ItsTime5.Controller;

import ItsTime5.Service.StudyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StudyMemberApiController {

    private final StudyMemberService studyMemberService;

}
