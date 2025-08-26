package com.todoservice.greencatsoftware.domain.project;

import com.todoservice.greencatsoftware.common.baseResponse.BaseResponseStatus;
import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.enums.Visibility;
import com.todoservice.greencatsoftware.common.exception.BaseException;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProjectTest {

    private Member member = Member.create("member1@test.com", "12345", "null", "testName");

    @Test
    @DisplayName("정상 생성: 기간 없이 생성")
    public void create_without_period_ok() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");

        //when
        Project project = Project.create(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                "   잘해보자구~",
                true,
                Visibility.PUBLIC);

        //then
        assertThat(project.getName()).isEqualTo("나의 프로젝트");
        assertThat(project.getStatus()).isEqualTo(Status.PLANNING);
        assertThat(project.getDescription()).isEqualTo("잘해보자구~");
        assertThat(project.getIsPublic()).isTrue();
        assertThat(project.getVisibility()).isEqualTo(Visibility.PUBLIC);
        assertThat(project.getColor().getHexCode()).isEqualTo("FF0000");
        assertThat(project.getColor().getName()).isEqualTo("RED");
    }
    
    @Test
    @DisplayName("저장 생성: 기간 포함 생성")
    public void create_with_period_ok() throws Exception {
        //given
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        Color color = Color.create("RED", "FF0000");
        Period period = Period.of(startDate, endDate, null);

        //when
        Project project = Project.createWithPeriod(
                color,
                member,
                 "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~",
                true,
                Visibility.PUBLIC);

        //then
        assertThat(project.getName()).isEqualTo("나의 프로젝트");
        assertThat(project.getStatus()).isEqualTo(Status.PLANNING);
        assertThat(project.getPeriod().startDate()).isEqualTo(startDate);
        assertThat(project.getPeriod().endDate()).isEqualTo(endDate);
        assertThat(project.getPeriod().actualEndDate()).isNull();
        assertThat(project.getDescription()).isEqualTo("잘해보자구~");
        assertThat(project.getIsPublic()).isTrue();
        assertThat(project.getVisibility()).isEqualTo(Visibility.PUBLIC);
        assertThat(project.getColor().getHexCode()).isEqualTo("FF0000");
        assertThat(project.getColor().getName()).isEqualTo("RED");
    }

    @Test
    @DisplayName("검증 실패: color, name, status, isPublic, visibility가 null 이면 BaseException 반환")
    public void create_fail_validation() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");

        //then
        //color null
        assertThatThrownBy(() -> Project.create(
                null, member,"name", Status.PLANNING, "description", true, Visibility.PUBLIC
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT.getMessage());

        //name empty
        assertThatThrownBy(() -> Project.create(
                color, member,"", Status.PLANNING, "description", true, Visibility.PUBLIC
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT.getMessage());

        //name toLong
        String longName = "a".repeat(101);
        assertThatThrownBy(() -> Project.create(
                color, member,longName, Status.PLANNING, "description", true, Visibility.PUBLIC
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT.getMessage());

        //status null
        assertThatThrownBy(() -> Project.create(
                color, member,"name", null, "description", true, Visibility.PUBLIC
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT.getMessage());

        //isPublic null
        assertThatThrownBy(() -> Project.create(
                color, member,"name", Status.PLANNING, "description", null, Visibility.PUBLIC
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_IS_PUBLIC_FOR_PROJECT.getMessage());

        //visibility null
        assertThatThrownBy(() -> Project.create(
                color, member,"name", Status.PLANNING, "description", true, null
        )).isInstanceOf(BaseException.class).hasMessage(BaseResponseStatus.MISSING_VISIBILITY_FOR_PROJECT.getMessage());
    }

    @Test
    @DisplayName("수정 로직: color/name/status/period/description/isPublic/visibility")
    public void change_fields() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(
                color,
                member,
                "   나의 프로젝트    ",
                Status.PLANNING,
                period,
                "   잘해보자구~",
                true,
                Visibility.PUBLIC);

        //when
        project.changeColor(Color.create("BLUE", "0000FF"));
        project.changeName(" 변경됨      ");
        project.changeStatus(Status.COMPLETED);
        project.changePeriod(Period.of(startDate.plusDays(1), endDate.minusDays(5), actualEndDate.minusDays(5)));
        project.changeDescription("  내용이 변경 되었어요  ");
        project.changeIsPublic(false);
        project.changeVisibility(Visibility.PRIVATE);

        //then
        assertThat(project.getColor().getName()).isEqualTo("BLUE");
        assertThat(project.getColor().getHexCode()).isEqualTo("0000FF");
        assertThat(project.getName()).isEqualTo("변경됨");
        assertThat(project.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(project.getPeriod().startDate()).isEqualTo(LocalDate.of(2025, 1, 2));
        assertThat(project.getPeriod().endDate()).isEqualTo(LocalDate.of(2025, 12, 26));
        assertThat(project.getPeriod().actualEndDate()).isEqualTo(LocalDate.of(2025, 12, 26));
        assertThat(project.getDescription()).isEqualTo("내용이 변경 되었어요");
        assertThat(project.getIsPublic()).isFalse();
        assertThat(project.getVisibility()).isEqualTo(Visibility.PRIVATE);
    }

    @Test
    @DisplayName("수정 실패: 잘못된 값이 들어오면 BaseException 반환")
    public void change_fail() throws Exception {
        //given
        Color color = Color.create("RED", "FF0000");
        Project project = Project.create(
                color,
                member,
                "name",
                Status.PLANNING,
                "description",
                true,
                Visibility.PUBLIC);

        //then
        assertThatThrownBy(() -> project.changeColor(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_COLOR_FOR_PROJECT.getMessage());

        assertThatThrownBy(() -> project.changeName(""))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_TITLE_FOR_PROJECT.getMessage());

        String longName = "a".repeat(101);
        assertThatThrownBy(() -> project.changeName(longName))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.TITLE_EXCEEDS_LIMIT_FOR_PROJECT.getMessage());

        assertThatThrownBy(() -> project.changeStatus(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_STATUS_FOR_PROJECT.getMessage());

        assertThatThrownBy(() -> project.changeIsPublic(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_IS_PUBLIC_FOR_PROJECT.getMessage());

        assertThatThrownBy(() -> project.changeVisibility(null))
                .isInstanceOf(BaseException.class)
                .hasMessage(BaseResponseStatus.MISSING_VISIBILITY_FOR_PROJECT.getMessage());
    }
}
