package com.gfo.gfo_meesterproef.Support;

import java.util.Arrays;
import java.util.List;

public class Converter {

//    splits comma-separated String result to List<String> result
    public List<String> splitStringToList(String result, String borderMark){
        String[] splitResultArray = result.split(borderMark);
        List<String> splitResultList = (Arrays.asList(splitResultArray));
    return splitResultList;}
}