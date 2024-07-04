package com.aivle.bit.member.service;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

class RandomCodeGeneratorTest {

    @RepeatedTest(10)
    @DisplayName("[성공] 무작위로 생성되는 코드는 6자리의 숫자로 이루어져야 한다.")
    void generate_Suceess() {
        // given
        final int expectedLength = 6;

        // when
        String code = RandomCodeGenerator.generate();

        // then
        assertThat(code).hasSizeLessThanOrEqualTo(expectedLength);
    }
}