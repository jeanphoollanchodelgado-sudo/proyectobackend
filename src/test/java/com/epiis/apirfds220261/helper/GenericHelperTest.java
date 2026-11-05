package com.epiis.apirfds220261.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GenericHelperTest {

    @Test
    void followCodeGenerationReturnsSevenUppercaseLettersOrNumbers() {
        String code = GenericHelper.followCodeGeneration();

        assertEquals(7, code.length());
        assertTrue(code.matches("[0-9A-Z]{7}"));
    }
}