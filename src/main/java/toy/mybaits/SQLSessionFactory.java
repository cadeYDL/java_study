package toy.mybaits;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SQLSessionFactory {
    private  final String JDBCURL;
    private  final String USER;
    private  final String PASSWORD;
    public SQLSessionFactory(String url,String user,String password){
        this.JDBCURL =  url;
        this.PASSWORD = password;
        this.USER = user;
    }

    public  <T> T getMapper(Class<T> mapperClass) {
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperClass},new MapperInvocationHandler());
    }

    class MapperInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().startsWith("select")) {
                return invokeSelect(proxy,method,args);
            }
            return method.invoke(this,args);
        }



        private Object invokeSelect(Object proxy, Method method, Object[] args) {
            String sql = createSQL(method);
            System.out.println("exec: "+sql);
            try (Connection conn = DriverManager.getConnection(JDBCURL, USER, PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                if (args!=null){
                    for (int i = 0; i < args.length; i++) {
                        Object arg = args[i];
                        if (arg instanceof Integer) {
                            statement.setInt(i + 1, (int) arg);
                        } else if (arg instanceof String) {
                            statement.setString(i + 1, arg.toString());
                        }
                    }
                }

                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    return parseResult(rs, method.getReturnType());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

        private Object parseResult(ResultSet rs, Class<?> returnType) throws Exception {
            if (!returnType.isArray()){
                return parseSingleResult(rs,returnType);
            }
            ArrayList<Object> list = new ArrayList<>();
            do{
                list.add(parseSingleResult(rs, returnType.getComponentType()));
            }while (rs.next());
            Object obj = Array.newInstance(returnType.getComponentType(), list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(obj, i, list.get(i));
            }
            return obj;

        }

        private Object parseSingleResult(ResultSet rs, Class<?> returnType) throws Exception {
            Object obj = returnType.getConstructor().newInstance();
            Field[] fields = returnType.getDeclaredFields();
            for (Field field : fields) {
                Object columnValue = null;
                String fieldName = field.getName();
                switch (field.getType().getSimpleName()) {
                    case "String":
                        columnValue = rs.getString(fieldName);
                        break;
                    case "int","Integer":
                        columnValue = rs.getInt(fieldName);
                        break;
                    default:
                        continue;
                }
                field.setAccessible(true);
                field.set(obj, columnValue);
            }
            return obj;
        }

        private String createSQL(Method method) {
            Class<?> returnType = method.getReturnType();
            if (returnType.isArray()) {
                returnType = returnType.getComponentType();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            List<String> selectCols = getSelectCols(returnType.getDeclaredFields());
            sb.append(String.join(", ", selectCols));
            sb.append(" FROM ");
            String tableName = getTableName(returnType);
            sb.append(tableName);
            String whereStr = getSelectWhere(method.getParameters());
            if (whereStr != null && !whereStr.isEmpty()) {
                sb.append(" WHERE ");
                sb.append(whereStr);
            }

            return sb.toString();
        }

        private String getSelectWhere(Parameter[] parameters) {
            return Arrays.stream(parameters).map((p)-> p.getAnnotation(TableParam.class).name()+" = ?").collect(Collectors.joining(" AND "));
        }

        private String getTableName(Class<?> returnType) {
            return returnType.getAnnotation(Table.class).tableName();
        }

        private List<String> getSelectCols(Field[] files) {
            return Arrays.stream(files).map(Field::getName).collect(Collectors.toList());
        }
    }
}
