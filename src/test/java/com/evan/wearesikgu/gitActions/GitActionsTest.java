package com.evan.wearesikgu.gitActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GitActionsTest {
    @Test
    @DisplayName("test 테스트")
    public void test() throws Exception {
        //given
        int result = 2;
        //when

        //then
        assertThat(result).isEqualTo(2);
    }
}
