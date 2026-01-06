package toy.mybaits;
import lombok.ToString;

@ToString
@Table(tableName ="user")
public class User {
    public int id;
    public String name;
    public int age;
}
