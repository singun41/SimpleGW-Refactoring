package com.project.simplegw.member.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.simplegw.member.dtos.send.DtosEmployeesProfile;
import com.project.simplegw.member.helpers.MemberConverter;
import com.project.simplegw.upload.services.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {
    private final MemberService service;
    private final MemberConverter converter;
    private final ImageService imgService;

    public EmployeeService(MemberService serevice, MemberConverter converter, ImageService imgService) {
        this.service = serevice;
        this.converter = converter;
        this.imgService = imgService;

        log.info("Component '" + this.getClass().getName() + "' has been created.");
    }





    public List<DtosEmployeesProfile> getEmployeesProfile(String team) {
        return service.getTeamMembers(team).stream().map(
            e -> converter.getEmployeesProfile( service.getProfile(e.getId()).calcDuration() ).setPortrait( imgService.getPortrait(e.getId()) )
        ).collect(Collectors.toList());
    }
}
