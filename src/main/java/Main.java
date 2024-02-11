import dao.UserDaoImpl;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args)  {
        UserDaoImpl userDao = new UserDaoImpl();

        List<Long> members = new ArrayList<>();
        members.add(2L);
        members.add(3L);
        members.add(4L);

        userDao.createGroupChat(1L,"Second group chat", members);

    }
}
