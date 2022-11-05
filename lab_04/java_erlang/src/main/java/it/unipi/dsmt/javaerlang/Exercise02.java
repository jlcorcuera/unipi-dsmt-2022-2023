package it.unipi.dsmt.javaerlang;

import java.util.ArrayList;
import java.util.List;

public class Exercise02 {

    public static void main(String[] args){
        List<Long> numbers = new ArrayList<>();
        numbers.add(121L);
        numbers.add(56L);
        numbers.add(116L);
        numbers.add(15951L);
        Long result = PalindromicUtils.sum(numbers);
        System.out.println("The result is: " + result);
    }
}
