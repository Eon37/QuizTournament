package com.company.Quiz_Tournament.api.Quiz;

import com.company.Quiz_Tournament.constants.CommonConstants;
import com.company.Quiz_Tournament.constants.EmptyQuizConstants;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class QuizTest {

    @Test
    void emptyQuizOptionsSize() {
        assertEquals(Quiz.newEmptyQuiz().getOptions().size(), CommonConstants.DEFAULT_INT_OPTIONS_SIZE);
    }

    @Test
    void addEmptyOptions() {
        //Given
        Collection<String> initList = Arrays.asList("1", "2", "3");

        //When
        Collection<String> newList = Quiz.addEmptyOptions(initList, CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

        //Then
        assertEquals(CommonConstants.DEFAULT_INT_OPTIONS_SIZE, newList.size());
        assertTrue(newList.containsAll(initList));
        assertEquals(CommonConstants.DEFAULT_INT_OPTIONS_SIZE - initList.size(),
                newList.stream().filter(EmptyQuizConstants.OPTION::equals).count());
    }

    @Test
    void addEmptyOptionsWhenAllFilled() {
        //Given
        Collection<String> initList = IntStream
                .range(0, CommonConstants.DEFAULT_INT_OPTIONS_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());

        //When
        Collection<String> newList = Quiz.addEmptyOptions(initList, CommonConstants.DEFAULT_INT_OPTIONS_SIZE);

        //Then
        assertEquals(CommonConstants.DEFAULT_INT_OPTIONS_SIZE, newList.size());
        assertFalse(newList.contains(EmptyQuizConstants.OPTION));
    }
}
