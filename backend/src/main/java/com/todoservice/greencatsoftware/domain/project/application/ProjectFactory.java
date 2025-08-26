package com.todoservice.greencatsoftware.domain.project.application;

import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.color.application.ColorService;
import com.todoservice.greencatsoftware.domain.member.application.MemberService;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectFactory {
    private final MemberService memberService;
    private final ColorService colorService;

    public Project createProject(ProjectCreateRequest request) {
        String jwt = "1 test@email 1234 null testname";
        Member member = memberService.getMemberByIdOrThrow(Long.parseLong(jwt.substring(0, 1)));
        Color color = colorService.getColorByIdOrThrow(request.colorId());
        Period period = Period.of(request.period().startDate(), request.period().endDate(),
                                  request.period().actualEndDate());

        return (period.isNull())
                ? Project.create(color, member, request.name(), request.status(),
                request.description(), request.isPublic(), request.visibility())

                : Project.createWithPeriod(color, member, request.name(), request.status(),
                period, request.description(), request.isPublic(), request.visibility());
    }
}