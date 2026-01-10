package toy.mybaits;

public class Main {
    public static void main(String[] args) {
        UserMapper mapper = new SQLSessionFactory("jdbc:mysql://192.168.139.98:3306/test","root","123456").
                getMapper(UserMapper.class);
        System.out.println(mapper.hashCode());
        User[] users = mapper.selectAllUsers();
        if (users != null) {
            for (User user : users) {
                System.out.println(user);
            }
        }
        User user = mapper.selectUserById(1);
        System.out.println(user);
        user = mapper.selectUserByName("tom");
        System.out.println(user);
        user = mapper.selectUserByAgeAndName(11,"tom");
        System.out.println(user);
        user = mapper.selectUserByAgeAndName(17,"tom");
        System.out.println(user);
    }
}
