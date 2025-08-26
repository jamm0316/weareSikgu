package com.todoservice.greencatsoftware.domain.task;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.enums.DayLabel;
import com.todoservice.greencatsoftware.common.enums.Priority;
import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.enums.Visibility;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import com.todoservice.greencatsoftware.domain.task.domain.entity.Task;
import com.todoservice.greencatsoftware.domain.task.domain.vo.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskTest {

    private Color color(String name, String hexCode) {
        return Color.create(name, hexCode);
    }
    private Member member = Member.create("member1@test.com", "12345", "null", "testName");

    private Project projectWithPeriod() {
        return Project.createWithPeriod(color("RED", "#FF0000"), member, "프로젝트", Status.PLANNING,
                Period.of(LocalDate.of(2005, 1, 1), LocalDate.of(2005, 12, 31), LocalDate.of(2005, 12, 31)),
                "description", true, Visibility.PUBLIC);
    }

    private Project project() {
        return Project.create(color("RED", "#FF0000"), member,"프로젝트", Status.PLANNING,
                "description", true, Visibility.PUBLIC);
    }

    @Test
    @DisplayName("정상 생성: 스케쥴 포함")
    public void createWithoutScheduleOk() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        Schedule schedule = Schedule.of(startDate, startTime, true, endDate, null, false);
        Task task = Task.createWithSchedule(projectWithPeriod(), Priority.HIGH, "해야할 일",
                "이러저렇게 한다", DayLabel.MORNING, schedule, Status.PLANNING);

        //then
        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task.getTitle()).isEqualTo("해야할 일");
        assertThat(task.getDescription()).isEqualTo("이러저렇게 한다");
        assertThat(task.getDayLabel()).isEqualTo(DayLabel.MORNING);
        assertThat(task.getSchedule().startDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(task.getSchedule().startTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(task.getSchedule().dueDate()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(task.getSchedule().dueTimeEnabled()).isFalse();
        assertThat(task.getSchedule().startTimeEnabled()).isTrue();
        assertThat(task.getStatus()).isEqualTo(Status.PLANNING);
    }

    @Test
    @DisplayName("정상 생성: 스케쥴 없음, Schedule.noSchedule()로 셋팅")
    public void createWithScheduleOk() throws Exception {
        //given
        Schedule noSchedule = Schedule.noSchedule();
        Task task = Task.createWithSchedule(project(), Priority.HIGH, "해야할 일",
                "이러저렇게 한다", DayLabel.MORNING, noSchedule, Status.PLANNING);

        //then
        assertThat(task.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(task.getTitle()).isEqualTo("해야할 일");
        assertThat(task.getDescription()).isEqualTo("이러저렇게 한다");
        assertThat(task.getDayLabel()).isEqualTo(DayLabel.MORNING);
        assertThat(task.getSchedule().startDate()).isNull();
        assertThat(task.getSchedule().startTime()).isNull();
        assertThat(task.getSchedule().dueDate()).isNull();
        assertThat(task.getSchedule().dueTime()).isNull();
        assertThat(task.getSchedule().dueTimeEnabled()).isFalse();
        assertThat(task.getSchedule().startTimeEnabled()).isFalse();
        assertThat(task.getStatus()).isEqualTo(Status.PLANNING);
    }

    @Test
    @DisplayName("검증 실패: project/color/title/priority/status 누락")
    public void createFailValidation() throws Exception {
        //given
        Project project = project();

        //then
        assertThatThrownBy(() -> Task.create(null, Priority.HIGH,
                "알고리즘 공부", null, DayLabel.MORNING, Status.PLANNING))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PROJECT_FOR_TASK.getMessage());

        assertThatThrownBy(() -> Task.create(project, null,
                "알고리즘 공부", null, DayLabel.MORNING, Status.PLANNING))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PRIORITY_FOR_TASK.getMessage());

        assertThatThrownBy(() -> Task.create(project, Priority.HIGH,
                null, null, DayLabel.MORNING, Status.PLANNING))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_TITLE_FOR_TASK.getMessage());

        String longTitle = "a".repeat(101);
        assertThatThrownBy(() -> Task.create(project, Priority.HIGH,
                longTitle, null, DayLabel.MORNING, Status.PLANNING))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_TASK.getMessage());

        assertThatThrownBy(() -> Task.create(project, Priority.HIGH,
                "알고리즘 공부", null, null, null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_STATUS_FOR_TASK.getMessage());
    }

    @Test
    @DisplayName("수정 로직: project/color/priority/title/description/dayLabel/schedule/status")
    public void changeFields() throws Exception {
        //given
        Task task = Task.create(project(), Priority.HIGH,
                "알고리즘 공부", "백준123", DayLabel.MORNING, Status.PLANNING);

        Project newProject = Project.create(color("BLUE", "#0000FF"), member,"새로운 프로젝트",
                Status.COMPLETED, "new description", true, Visibility.TEAM);

        Color newColor = Color.create("GREEN", "#00FF00");

        LocalDate startDate = LocalDate.of(2025, 1, 10);
        Schedule schedule = Schedule.of(startDate, null, false, null, null, false);

        //when
        task.changeProject(newProject);
        task.changePriority(Priority.MEDIUM);
        task.changeTitle("알고리즘 복습");
        task.changeDescription("백준123 완료, 다음주에 복습하기");
        task.changeDayLabel(DayLabel.AFTERNOON);
        task.changeSchedule(schedule);
        task.changeStatus(Status.ON_HOLD);

        //then
        assertThat(task.getProject().getName()).isEqualTo("새로운 프로젝트");
        assertThat(task.getProject().getDescription()).isEqualTo("new description");
        assertThat(task.getProject().getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(task.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(task.getTitle()).isEqualTo("알고리즘 복습");
        assertThat(task.getDescription()).isEqualTo("백준123 완료, 다음주에 복습하기");
        assertThat(task.getDayLabel()).isEqualTo(DayLabel.AFTERNOON);
        assertThat(task.getSchedule().startDate()).isEqualTo(LocalDate.of(2025, 1, 10));
        assertThat(task.getSchedule().startTime()).isNull();
        assertThat(task.getSchedule().dueDate()).isNull();
        assertThat(task.getSchedule().dueTime()).isNull();
        assertThat(task.getSchedule().dueTimeEnabled()).isFalse();
        assertThat(task.getSchedule().startTimeEnabled()).isFalse();
        assertThat(task.getStatus()).isEqualTo(Status.ON_HOLD);
    }

    @Test
    @DisplayName("수정 실패: null 값")
    public void changeFail() throws Exception {
        //given
        Task task = Task.create(project(), Priority.HIGH,
                "해야할 일", null, DayLabel.MORNING, Status.PLANNING);

        //then
        assertThatThrownBy(() -> task.changeProject(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PROJECT_FOR_TASK.getMessage());

        assertThatThrownBy(() -> task.changePriority(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PRIORITY_FOR_TASK.getMessage());

        assertThatThrownBy(() -> task.changeTitle(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_TITLE_FOR_TASK.getMessage());

        String longTitle = "a".repeat(101);
        assertThatThrownBy(() -> task.changeTitle(longTitle))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_TASK.getMessage());

        assertThatThrownBy(() -> task.changePriority(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_PRIORITY_FOR_TASK.getMessage());

        assertThatThrownBy(() -> task.changeStatus(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_STATUS_FOR_TASK.getMessage());
    }
}
