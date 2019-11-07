package info.cafeit.hibernatemanytomany;

import info.cafeit.hibernatemanytomany.model.Authority;
import info.cafeit.hibernatemanytomany.model.Users;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@SpringBootApplication
public class HibernateManyTomanyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(HibernateManyTomanyApplication.class, args);
    }

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Users user1 = new Users("longtran", "tranhuulongcit@gmail.com", "Long", "Tran");
        Users user2 = new Users("namtran", "namtran@gmail.com", "nam", "Tran");
        Users user3 = new Users("binhtran", "binh@gmail.com", "binh", "Tran");

        Authority authorityAdmin = new Authority("ADMIN", false);
        Authority authorityUser = new Authority("USER", false);
        Authority authoritySa = new Authority("SA", false);

        authorityAdmin.setUsers(Arrays.asList(user1, user2, user3));
        authorityUser.setUsers(Arrays.asList(user2, user3));
        authoritySa.setUsers(Collections.singletonList(user1));


        user1.setAuthorities(Arrays.asList(authorityAdmin, authoritySa));
        user2.setAuthorities(Arrays.asList(authorityAdmin, authorityUser));
        user3.setAuthorities(Arrays.asList(authorityUser, authorityAdmin));

        //lưu vào database
        //vì chúng ta đang config cascade = CascadeType.ALL nên nó sẻ lưu luôn Authority
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);

        //select Users và lấy ra tham chiếu của Authority
        Users p = entityManager.find(Users.class, 1l);

        System.out.println(p);
        p.getAuthorities().forEach(System.out::println);

        System.out.println("-------------------------------------");

        //ngược lại chúng ta có thể tìm được danh sách User thuộc Authority đó

        Authority admin = entityManager.find(Authority.class, 1l);
        System.out.println(admin);
        admin.getUsers().forEach(System.out::println);


    }
}
