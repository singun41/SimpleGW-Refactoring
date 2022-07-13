package com.project.simplegw.system.services;

import java.util.List;

import com.project.simplegw.alarm.dtos.send.DtosAlarm;
import com.project.simplegw.alarm.services.AlarmService;
import com.project.simplegw.code.dtos.send.DtosBasecode;
import com.project.simplegw.code.dtos.send.DtosCodeValue;
import com.project.simplegw.code.services.BasecodeService;
import com.project.simplegw.code.vos.BasecodeType;
import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocs;
import com.project.simplegw.document.approval.services.DefaultReportService;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.services.ArchiveService;
import com.project.simplegw.document.services.DocsFormService;
import com.project.simplegw.document.services.FreeboardService;
import com.project.simplegw.document.services.NoticeService;
import com.project.simplegw.document.services.SuggestionService;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.document.vos.EditorDocs;
import com.project.simplegw.member.data.MemberData;
import com.project.simplegw.member.dtos.admin.send.DtosMember;
import com.project.simplegw.member.dtos.admin.send.DtosMemberDetails;
import com.project.simplegw.member.dtos.send.DtosMyDetails;
import com.project.simplegw.member.services.MemberAdminService;
import com.project.simplegw.member.services.MemberClientService;
import com.project.simplegw.member.services.MemberService;
import com.project.simplegw.system.security.LoginUser;
import com.project.simplegw.upload.dtos.DtosAttachements;
import com.project.simplegw.upload.services.AttachmentsService;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class ViewService {   // ViewController에서 필요한 데이터들을 전달하기 위해 여기에 각 서비스들의 필요한 메서드만 선택해 작성한다.
    private final AlarmService alarmService;

    private final MemberService memberService;
    private final MemberClientService memberClientService;
    private final MemberAdminService memberAdminService;

    private final BasecodeService basecodeService;
    private final DocsFormService formService;
    private final AttachmentsService attachmentsService;

    // Boards..
    private final NoticeService noticeService;
    private final FreeboardService freeboardService;
    private final SuggestionService suggestionService;
    private final ArchiveService archiveService;

    // Approvals..
    private final DefaultReportService defaultReportService;

    // @Autowired   // framework 버전 업데이트 이후 자동설정되어 선언하지 않아도 됨.
    public ViewService(
        AlarmService alarmService,
        MemberService memberService, MemberClientService memberClientService, MemberAdminService memberAdminService,
        BasecodeService basecodeService, DocsFormService formService, AttachmentsService attachmentsService,
        NoticeService noticeService, FreeboardService freeboardService, SuggestionService suggestionService, ArchiveService archiveService,

        DefaultReportService defaultReportService
    ) {
        this.alarmService = alarmService;

        this.memberService = memberService;
        this.memberClientService = memberClientService;
        this.memberAdminService = memberAdminService;

        this.basecodeService = basecodeService;
        this.formService = formService;
        this.attachmentsService = attachmentsService;

        this.noticeService = noticeService;
        this.freeboardService = freeboardService;
        this.suggestionService = suggestionService;
        this.archiveService = archiveService;

        this.defaultReportService = defaultReportService;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- alarm ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public DtosAlarm getAlarm(Long id) {
        return alarmService.getAlarm(id);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- alarm ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- member ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public MemberData getMyInfo(LoginUser loginUser) {
        return memberService.getMemberData(loginUser);
    }

    public DtosMyDetails getMyDetails(LoginUser loginUser) {
        return memberClientService.getMyDetails(loginUser);
    }    



    public DtosMemberDetails getMemberDetails(Long memberId) {
        return memberAdminService.getMemberDetails(memberId);
    }

    public DtosMember getMember(Long memberId) {
        return memberAdminService.getMember(memberId);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- member ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- codes ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public List<BasecodeType> getBasecodeTypes() {
        return basecodeService.getAllTypes();
    }

    public DtosBasecode getCode(Long id) {
        return basecodeService.getCode(id);
    }

    public List<String> getJobTitles() {
        return basecodeService.getJobTitles();
    }

    public List<String> getTeams() {
        return memberService.getTeams();
    }

    public List<DtosCodeValue> getDayoffCodes() {
        return basecodeService.getDayoffCodes();
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- codes ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs common ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public String getDocsForm(EditorDocs docs) {
        return formService.getForm(docs);
    }

    public List<DtosAttachements> getAttachmentsList(Long docsId) {
        return attachmentsService.getAttachmentsList(docsId);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs common ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- board ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public DtosDocs getNotice(Long docsId) {
        return noticeService.getNotice(docsId);
    }

    public DtosDocs getTempNotice(Long docsId) {
        return noticeService.getTempNotice(docsId);
    }


    public DtosDocs getFreeboard(Long docsId) {
        return freeboardService.getFreeboard(docsId);
    }

    public DtosDocs getTempFreeboard(Long docsId) {
        return freeboardService.getTempFreeboard(docsId);
    }


    public DtosDocs getSuggestion(Long docsId) {
        return suggestionService.getSuggestion(docsId);
    }

    public DtosDocs getTempSuggestion(Long docsId) {
        return suggestionService.getTempSuggestion(docsId);
    }


    public DtosDocs getArchive(Long docsId) {   // 자료실은 임시저장 기능을 제공하지 않는다.
        return archiveService.getArchive(docsId);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- board ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //





    // ↓ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- approval ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↓ //
    public DtosApprovalDocs getDefaultApproval(DocsType docsType, Long docsId, LoginUser loginUser) {
        return defaultReportService.getDtosApprovalDocs(docsType, docsId, loginUser);
    }

    public DtosDocs getTempDefaultApproval(DocsType docsType, Long docsId) {
        return defaultReportService.getTemp(docsType, docsId);
    }
    // ↑ ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- approval ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ↑ //
}
