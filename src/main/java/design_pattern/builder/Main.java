package design_pattern.builder;

import design_pattern.builder.sql.SQL;
import design_pattern.builder.sql.helper.SetValues;
import design_pattern.builder.sql.helper.Values;
import design_pattern.builder.sql.helper.Where;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(SQL.newUpdateBuidler().
                from("user").set(SetValues.builder().set("name","ydl").build())
                .where(Where.builder().build()).build());
        System.out.println(SQL.newUpdateBuidler().
                from("user").set(SetValues.builder().set("name","ydl").build())
                .where(Where.builder().where("name = y").where("age = 12").build()).build());

        System.out.println(SQL.newDeleteBuidler().From("user").where(null).build());

        System.out.println(SQL.newDeleteBuidler().From("user").where(Where.builder().where("name = cd").build()).build());

        System.out.println(SQL.newInsertBuidler().Into("user").value(Values.builder("name").add(new Main.User("1",1,"www")).build()).build());

        System.out.println(SQL.newInsertBuidler().Into("user").value(Values.builder("name","age","address").add(new Main.User("1",1,"www")).build()).build());

        System.out.println(SQL.newInsertBuidler().Into("user").value(Values.builder().add(new Main.User("1",1,"www")).build()).build());

        System.out.println(SQL.newSelectBuidler().select().From("user").where(null).build());

        System.out.println(SQL.newSelectBuidler().select("name","id").From("user").where(null).build());

        System.out.println(SQL.newSelectBuidler().select("name","id").From("user").where(Where.builder().where("name = cd").build()).build());
    }

    static class User {
        String name;
        int age;
        String address;

        public User(String name, int age, String address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }
    }
}
