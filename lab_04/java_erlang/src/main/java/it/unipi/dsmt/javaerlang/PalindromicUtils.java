package it.unipi.dsmt.javaerlang;

import java.util.List;
import java.util.stream.Collectors;

public class PalindromicUtils {

    public static long sum(List<Long> numbers){
        return numbers.stream()
                .filter(x -> x.toString().equals(new StringBuilder(x.toString()).reverse().toString()))
                .collect(Collectors.summingLong(Long::longValue));
    }
}
