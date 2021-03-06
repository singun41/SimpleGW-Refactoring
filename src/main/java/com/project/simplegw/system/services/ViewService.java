package com.project.simplegw.system.services;

import java.util.List;

import com.project.simplegw.alarm.dtos.send.DtosAlarm;
import com.project.simplegw.alarm.services.AlarmService;
import com.project.simplegw.code.dtos.send.DtosBasecode;
import com.project.simplegw.code.dtos.send.DtosCodeValue;
import com.project.simplegw.code.services.BasecodeService;
import com.project.simplegw.code.vos.BasecodeType;
import com.project.simplegw.document.approval.dtos.send.DtosApprovalDocsCommon;
import com.project.simplegw.document.approval.dtos.send.details.dayoff.DtosDayoff;
import com.project.simplegw.document.approval.dtos.send.details.dayoff.DtosTempDayoff;
import com.project.simplegw.document.approval.services.DefaultReportService;
import com.project.simplegw.document.approval.services.details.DayoffService;
import com.project.simplegw.document.dtos.send.DtosDocs;
import com.project.simplegw.document.dtos.send.DtosDocsAddReferrer;
import com.project.simplegw.document.services.ArchiveService;
import com.project.simplegw.document.services.DocsFormService;
import com.project.simplegw.document.services.FreeboardService;
import com.project.simplegw.document.services.MinutesService;
import com.project.simplegw.document.services.NoticeService;
import com.project.simplegw.document.services.SuggestionService;
import com.project.simplegw.document.vos.DocsType;
import com.project.simplegw.document.vos.EditorDocs;
import com.project.simplegw.member.data.MemberData;
import com.project.simplegw.member.dtos.admin.send.DtosMember;
import com.project.simplegw.member.dtos.admin.send.DtosMemberDetails;
import com.project.simplegw.member.dtos.send.DtosProfile;
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
public class ViewService {   // ViewController?????? ????????? ??????????????? ???????????? ?????? ????????? ??? ??????????????? ????????? ???????????? ????????? ????????????.
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
    private final MinutesService minutesService;

    // Approvals..
    private final DefaultReportService defaultReportService;
    private final DayoffService dayoffService;

    // @Autowired   // framework ?????? ???????????? ?????? ?????????????????? ???????????? ????????? ???.
    public ViewService(
        AlarmService alarmService,
        MemberService memberService, MemberClientService memberClientService, MemberAdminService memberAdminService,
        BasecodeService basecodeService, DocsFormService formService, AttachmentsService attachmentsService,

        NoticeService noticeService, FreeboardService freeboardService, SuggestionService suggestionService, ArchiveService archiveService,
        MinutesService minutesService,

        DefaultReportService defaultReportService, DayoffService dayoffService
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
        this.minutesService = minutesService;

        this.defaultReportService = defaultReportService;
        this.dayoffService = dayoffService;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }



    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- alarm ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
    public DtosAlarm getAlarm(Long id) {
        return alarmService.getAlarm(id);
    }
    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- alarm ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //





    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- member ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
    public MemberData getMyInfo(LoginUser loginUser) {
        return memberService.getMemberData(loginUser);
    }

    public DtosProfile getProfile(LoginUser loginUser) {
        return memberClientService.getProfile(loginUser);
    }    



    public DtosMemberDetails getMemberDetails(Long memberId) {
        return memberAdminService.getMemberDetails(memberId);
    }

    public DtosMember getMember(Long memberId) {
        return memberAdminService.getMember(memberId);
    }
    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- member ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //





    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- codes ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
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
    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- codes ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //





    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs common ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
    public String getDocsForm(EditorDocs docs) {
        return formService.getForm(docs);
    }

    public List<DtosAttachements> getAttachmentsList(Long docsId) {
        return attachmentsService.getAttachmentsList(docsId);
    }
    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- docs common ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //





    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- board ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
    public DtosDocs getNotice(Long docsId) {
        return noticeService.getDocs(docsId);
    }

    public DtosDocs getTempNotice(Long docsId) {
        return noticeService.getTempDocs(docsId);
    }


    public DtosDocs getFreeboard(Long docsId) {
        return freeboardService.getDocs(docsId);
    }

    public DtosDocs getTempFreeboard(Long docsId) {
        return freeboardService.getTempDocs(docsId);
    }


    public DtosDocs getSuggestion(Long docsId) {
        return suggestionService.getDocs(docsId);
    }

    public DtosDocs getTempSuggestion(Long docsId) {
        return suggestionService.getTempDocs(docsId);
    }


    public DtosDocs getArchive(Long docsId) {   // ???????????? ???????????? ????????? ???????????? ?????????.
        return archiveService.getDocs(docsId);
    }


    public DtosDocsAddReferrer getMinutes(Long docsId, LoginUser loginUser) {   // ???????????? ???????????? ????????? ???????????? ?????????.
        return minutesService.getDocs(docsId, loginUser);
    }
    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- board ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //





    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- approval ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
    public DtosApprovalDocsCommon getDefaultApproval(DocsType docsType, Long docsId, LoginUser loginUser) {
        // ?????? ???????????? ?????? ?????? ???????????? ?????? ????????????????????? ???????????? ?????????.
        // ???????????? ?????? api??? ????????????, ???????????? ?????? ???????????? ???????????? ??????.
        return defaultReportService.getDocs(docsType, docsId, loginUser);
    }

    public DtosDocs getTempDefaultApproval(DocsType docsType, Long docsId) {
        return defaultReportService.getTemp(docsType, docsId);
    }



    
    public DtosDayoff getDayoffApproval(Long docsId, LoginUser loginUser) {
        return dayoffService.getDocs(docsId, loginUser);
    }

    public DtosTempDayoff getTempDayoffApproval(Long docsId) {
        return dayoffService.getTempDocs(docsId);
    }
    // ??? ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- approval ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ??? //
}
