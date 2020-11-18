package com.wiley.common;

import com.wiley.common.LoggerUtil;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TableUtils {
    public static <T> List<T> getTableData(ResultSet rs, Class<T> data) {
        List<T> tableData = new ArrayList<>();
        List<String> classFields = getResultSetColumnNames(rs);
        List<Method> methods = Arrays.stream(data.getMethods()).filter(method -> method.getName().startsWith("set")).collect(Collectors.toList());
        try {
            while (rs.next()) {
                T obj = data.newInstance();
                for (String field : classFields) {
                    String mName = "SET" + field.toUpperCase();
                    Optional<Method> value = methods.stream().filter(method -> method.getName().equalsIgnoreCase(mName)).findFirst();
                    if (value.isPresent()) {
                        value.get().invoke(obj, rs.getString(field));
                    }
                }
                tableData.add(obj);
            }
        } catch (Exception e) {
            LoggerUtil.log(Level.WARNING,e.getMessage());
        }
        return tableData;
    }

    private static List<String> getResultSetColumnNames(ResultSet resultSet) {
        List<String> resultSetColumns = new ArrayList<>();
        try {
            int colCount = resultSet.getMetaData().getColumnCount();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            for (int i = 1; i <= colCount; i++) {
                resultSetColumns.add(rsmd.getColumnName(i));
            }
        } catch (Exception e) {
            LoggerUtil.log(Level.WARNING, e.getMessage());
        }
        return resultSetColumns;

    }
}
