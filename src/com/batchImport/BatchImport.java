package com.batchImport;

import com.batchImport.utils.CommonUtils;

import java.sql.Connection;

public class BatchImport {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = CommonUtils.getConnection("jdbc:mysql://localhost:3306/default", "root", "root123");

            boolean doneProcessFile = CommonUtils.processFile(connection);

            System.out.println("doneProcessFile: " + doneProcessFile);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
