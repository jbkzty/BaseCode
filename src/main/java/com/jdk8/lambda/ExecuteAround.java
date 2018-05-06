package com.jdk8.lambda;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author spuerKun
 * @date 17/12/10.
 */
public class ExecuteAround {

    public static void main(String[] args) throws IOException {

        String result = processFileLimited();
        System.out.println(result);
        System.out.println("---");

        String oneLine = processFile((BufferedReader br) -> br.readLine());
        System.out.println(oneLine);

        String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
        System.out.println(twoLine);
    }

    public static String processFileLimited() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/junjin4838/Documents/lambda.text"))) {
            return br.readLine();
        }
    }

    public static String processFile(BufferReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/junjin4838/Documents/lambda.text"))) {
            return p.process(br);
        }
    }

    public interface BufferReaderProcessor {
        String process(BufferedReader b) throws IOException;
    }
}
