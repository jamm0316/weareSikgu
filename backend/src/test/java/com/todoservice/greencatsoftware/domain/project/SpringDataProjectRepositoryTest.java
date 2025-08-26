package com.todoservice.greencatsoftware.domain.project;

import com.todoservice.greencatsoftware.common.enums.Status;
import com.todoservice.greencatsoftware.common.enums.Visibility;
import com.todoservice.greencatsoftware.domain.color.entity.Color;
import com.todoservice.greencatsoftware.domain.color.infrastructure.persistence.SpringDataColorRepository;
import com.todoservice.greencatsoftware.domain.member.domain.entity.Member;
import com.todoservice.greencatsoftware.domain.member.infrastructure.persistence.SpringDataMemberJpaRepository;
import com.todoservice.greencatsoftware.domain.project.domain.entity.Project;
import com.todoservice.greencatsoftware.domain.project.domain.vo.Period;
import com.todoservice.greencatsoftware.domain.project.infrastructure.persistence.SpringDataProjectJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SpringDataProjectRepositoryTest {
    @Autowired
    SpringDataProjectJpaRepository projectRepository;

    @Autowired
    SpringDataColorRepository colorRepository;

    @Autowired
    SpringDataMemberJpaRepository memberRepository;

    @Autowired
    EntityManager em;

    private Member member;
    @BeforeEach
    public void setUp() throws Exception {
        member = Member.create("member1@test.com", "Ghwimreik12@", "null", "testName");
        memberRepository.saveAndFlush(member);
    }


    @Test
    @DisplayName("저장 & 조회")
    public void save_and_find() throws Exception {
        //given
        Color color = colorRepository.saveAndFlush(Color.create("RED", "#FF0000"));
        Project project = Project.create(
                color,
                member,
                "프로젝트A",
                Status.PLANNING,
                "프로젝트A 입니다.",
                true,
                Visibility.PRIVATE);

        //when
        Project saved = projectRepository.saveAndFlush(project);
        em.clear();
        Project found = projectRepository.findById(saved.getId()).orElseThrow();

        //then
        assertThat(found.getId()).isNotNull();
        assertThat(found.getColor().getName()).isEqualTo("RED");
        assertThat(found.getColor().getHexCode()).isEqualTo("#FF0000");
        assertThat(found.getName()).isEqualTo("프로젝트A");
        assertThat(found.getStatus()).isEqualTo(Status.PLANNING);
        assertThat(found.getDescription()).isEqualTo("프로젝트A 입니다.");
        assertThat(found.getIsPublic()).isTrue();
        assertThat(found.getVisibility()).isEqualTo(Visibility.PRIVATE);
    }

    @Test
    @DisplayName("수정(더티체킹): 조회, 변경, flush 시 반영")
    public void update_dirty_checking() throws Exception {
        //given
        Color color = colorRepository.saveAndFlush(Color.create("RED", "#FF0000"));
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(
                color,
                member,
                "프로젝트A",
                Status.PLANNING,
                period,
                "프로젝트A 입니다.",
                true,
                Visibility.PRIVATE
        );

        Project saved = projectRepository.saveAndFlush(project);

        Project found = projectRepository.findById(saved.getId()).orElseThrow();

        //when
        LocalDate newStartDate = LocalDate.of(2025, 1, 10);
        LocalDate newEndDate = LocalDate.of(2025, 12, 30);
        LocalDate newActualEndDate = LocalDate.of(2025, 12, 29);

        found.changeColor(Color.create("BLUE", "#0000FF"));
        found.changeName("   새로운 프로젝트   ");
        found.changePeriod(Period.of(newStartDate, newEndDate, newActualEndDate));
        found.changeStatus(Status.COMPLETED);
        found.changeDescription(" 변경 된 프로젝트입니다.   ");
        found.changeIsPublic(false);
        found.changeVisibility(Visibility.TEAM);

        //then
        assertThat(found.getColor().getName()).isEqualTo("BLUE");
        assertThat(found.getColor().getHexCode()).isEqualTo("#0000FF");
        assertThat(found.getName()).isEqualTo("새로운 프로젝트");
        assertThat(found.getPeriod().startDate()).isEqualTo(LocalDate.of(2025, 1, 10));
        assertThat(found.getPeriod().endDate()).isEqualTo(LocalDate.of(2025, 12, 30));
        assertThat(found.getPeriod().actualEndDate()).isEqualTo(LocalDate.of(2025, 12, 29));
        assertThat(found.getStatus()).isEqualTo(Status.COMPLETED);
        assertThat(found.getDescription()).isEqualTo("변경 된 프로젝트입니다.");
        assertThat(found.getIsPublic()).isFalse();
        assertThat(found.getVisibility()).isEqualTo(Visibility.TEAM);
    }

    @Test
    @DisplayName("삭제")
    public void delete() throws Exception {
        //given
        Color color = colorRepository.saveAndFlush(Color.create("RED", "#FF0000"));
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDate actualEndDate = LocalDate.of(2025, 12, 31);
        Period period = Period.of(startDate, endDate, actualEndDate);
        Project project = Project.createWithPeriod(
                color,
                member,
                "삭제용",
                Status.PLANNING,
                period,
                "삭제할 프로젝트",
                true,
                Visibility.PRIVATE
        );
        Project saved = projectRepository.saveAndFlush(project);

        //when
        projectRepository.deleteById(saved.getId());
        projectRepository.flush();

        //then
        assertThat(projectRepository.findById(saved.getId())).isEmpty();
    }
}
