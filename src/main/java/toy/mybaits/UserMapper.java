package toy.mybaits;

public interface UserMapper {
    User selectUserByName(@TableParam(name="name") String name);
    User selectUserById(@TableParam(name="id")int id);
    User selectUserByAgeAndName(@TableParam(name="age")int age, @TableParam(name="name")String name);
    User[] selectAllUsers();
}
