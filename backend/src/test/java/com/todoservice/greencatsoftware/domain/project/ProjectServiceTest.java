package com.todoservice.greencatsoftware.domain.project;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.enums.Visibility;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.domain.color.application.ColorService;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.project.application.ProjectFactory;
import com.todoservice.greencatsoftware.domain.project.application.ProjectService;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.port.ProjectRepository;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectCreateRequest;
import com.todoservice.greencatsoftware.domain.project.presentation.dto.ProjectFieldUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ColorService colorService;

    @Mock
    private ProjectFactory factory;

    @InjectMocks
    private ProjectService projectService;

    private Member member = Member.create("member1@test.com", "12345", "null", "testName");

    @Test
    @DisplayName("listProject: 전체 조회")
    public void listProject() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project projectA = Project.createWithPeriod(Color.create("RED", "#FF0000"),member, "프로젝트 A", Status.PLANNING,
                period, "프로젝트 A입니다", true, Visibility.PRIVATE);
        Project projectB = Project.createWithPeriod(Color.create("RED", "#FF0000"), member,"프로젝트 B", Status.PLANNING,
                period, "프로젝트 B입니다", true, Visibility.PRIVATE);

        //when
        when(projectRepository.findAll()).thenReturn(List.of(projectA, projectB));

        //then
        assertThat(projectService.listProject()).hasSize(2);
        verify(projectRepository).findAll();
    }
    
    @Test
    @DisplayName("getProjectByIdThrow: 존재, 반환 / 미존재, BaseException 반환")
    public void getByIdOrThrow() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(Color.create("RED", "#FF000000"), member, "프로젝트 A", Status.PLANNING,
                period, "프로젝트 A입니다", true, Visibility.PRIVATE);

        //when
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.findById(9L)).thenReturn(Optional.empty());

        //then
        assertThat(projectService.getProjectByIdOrThrow(1L)).isSameAs(project);
        assertThatThrownBy(() -> projectService.getProjectByIdOrThrow(9L))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.NOT_FOUND_PROJECT.getMessage());
    }

    @Test
    @DisplayName("createProject: Factory가 조립한 엔티티를 save한다.")
    public void createProject() throws Exception {
        //given
        ProjectCreateRequest request = mock(ProjectCreateRequest.class);
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(Color.create("RED", "#FF000000"), member, "프로젝트 A", Status.PLANNING,
                period, "프로젝트 A입니다", true, Visibility.PRIVATE);

        //when
        when(factory.createProject(request)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project saved = projectService.createProject(request);

        //then
        assertThat(saved).isEqualTo(project);
        verify(factory).createProject(request);
        verify(projectRepository).save(project);
    }

    @Test
    @DisplayName("deleteProject: 삭제")
    public void delete() throws Exception {
        //then
        projectService.deleteProjectById(3L);
        verify(projectRepository).deleteById(3L);
    }

    @Test
    @DisplayName("updateProjectField: 프로젝트 이름만 업데이트")
    public void updateProjectField_Name() throws Exception {
        // given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);

        Period originalPeriod = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(
                Color.create("RED", "#FF000000"),
                member,
                "프로젝트 A",
                Status.PLANNING,
                originalPeriod,
                "프로젝트 A입니다",
                true,
                Visibility.PRIVATE
        );

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Map<String, Object> newPeriodMap = new HashMap<>();
        newPeriodMap.put("startDate", "2025-02-01");   // 혹은 LocalDate.of(2025,2,1)
        newPeriodMap.put("endDate",   "2025-11-30");
        newPeriodMap.put("actualEndDate", null);

        ProjectFieldUpdateRequest request = new ProjectFieldUpdateRequest("period", newPeriodMap);

        // when
        Project updated = projectService.updateProjectField(1L, request);

        // then
        assertThat(updated.getPeriod().startDate()).isEqualTo(LocalDate.of(2025, 2, 1));
        assertThat(updated.getPeriod().endDate()).isEqualTo(LocalDate.of(2025, 11, 30));
        assertThat(updated.getPeriod().actualEndDate()).isNull(); // null 유지 확인

        assertThat(updated.getName()).isEqualTo("프로젝트 A");
        assertThat(updated.getDescription()).isEqualTo("프로젝트 A입니다");
        assertThat(updated.getStatus()).isEqualTo(Status.PLANNING);

        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateProjectField: 프로젝트 상태만 업데이트")
    public void updateProjectField_Status() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(Color.create("RED", "#FF000000"), member, "프로젝트 A", Status.PLANNING,
                period, "프로젝트 A입니다", true, Visibility.PRIVATE);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectFieldUpdateRequest request = new ProjectFieldUpdateRequest("status", "IN_PROGRESS");

        //when
        Project updated = projectService.updateProjectField(1L, request);

        //then
        assertThat(updated.getStatus()).isEqualTo(Status.IN_PROGRESS);
        assertThat(updated.getName()).isEqualTo("프로젝트 A");
        assertThat(updated.getDescription()).isEqualTo("프로젝트 A입니다");
        assertThat(updated.getVisibility()).isEqualTo(Visibility.PRIVATE);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateProjectField: 프로젝트 기간 업데이트")
    public void updateProjectField_Period() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period originalPeriod = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(Color.create("RED", "#FF000000"), member, "프로젝트 A", Status.PLANNING,
                originalPeriod, "프로젝트 A입니다", true, Visibility.PRIVATE);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        LinkedHashMap<String, Object> periodMap = new LinkedHashMap<>();
        periodMap.put("startDate", "2025-08-22");
        periodMap.put("endDate", "2025-08-23");
        periodMap.put("actualEndDate", null);
        ProjectFieldUpdateRequest request = new ProjectFieldUpdateRequest("period", periodMap);

        //when
        Project updated = projectService.updateProjectField(1L, request);

        //then
        assertThat(updated.getPeriod().startDate()).isEqualTo(LocalDate.of(2025, 8, 22));
        assertThat(updated.getPeriod().endDate()).isEqualTo(LocalDate.of(2025, 8, 23));
        assertThat(updated.getName()).isEqualTo("프로젝트 A");
        assertThat(updated.getDescription()).isEqualTo("프로젝트 A입니다");
        assertThat(updated.getStatus()).isEqualTo(Status.PLANNING);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateProjectField: 설명 업데이트 (null로 설정)")
    public void updateProjectField_Description_Null() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 12, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(Color.create("RED", "#FF000000"), member, "프로젝트 A", Status.PLANNING,
                period, "프로젝트 A입니다", true, Visibility.PRIVATE);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectFieldUpdateRequest request = new ProjectFieldUpdateRequest("description", null);

        //when
        Project updated = projectService.updateProjectField(1L, request);

        //then
        assertThat(updated.getDescription()).isNull();
        assertThat(updated.getName()).isEqualTo("프로젝트 A");
        assertThat(updated.getStatus()).isEqualTo(Status.PLANNING);
        assertThat(updated.getVisibility()).isEqualTo(Visibility.PRIVATE);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateProjectField: 색상 업데이트")
    public void updateProjectField_Color() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(Color.create("RED", "#FF000000"), member, "프로젝트 A", Status.PLANNING,
                period, "프로젝트 A입니다", true, Visibility.PRIVATE);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(colorService.getColorByIdOrThrow(99L)).thenReturn(Color.create("BLUE", "#0000FF"));

        ProjectFieldUpdateRequest request = new ProjectFieldUpdateRequest("colorId", 99L);

        //when
        Project updated = projectService.updateProjectField(1L, request);

        //then
        assertThat(updated.getColor().getName()).isEqualTo("BLUE");
        assertThat(updated.getColor().getHexCode()).isEqualTo("#0000FF");
        assertThat(updated.getName()).isEqualTo("프로젝트 A");
        assertThat(updated.getDescription()).isEqualTo("프로젝트 A입니다");
        assertThat(updated.getStatus()).isEqualTo(Status.PLANNING);
        verify(projectRepository, never()).save(any());
    }
}