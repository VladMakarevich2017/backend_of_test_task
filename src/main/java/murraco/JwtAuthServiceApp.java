package murraco;

import java.util.ArrayList;
import java.util.Arrays;

import murraco.model.Note;
import murraco.model.NoteType;
import murraco.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import murraco.model.Role;
import murraco.model.User;
import murraco.service.UserService;

@SpringBootApplication
public class JwtAuthServiceApp implements CommandLineRunner {

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthServiceApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... params) throws Exception {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@email.com");
        Note note1 = new Note();
        note1.setNote("sdasdasdasdasdasdas ad sd sd sd sd 123 as dasdas ");
        note1.setName("note1");
        note1.setType(NoteType.MEETING);
        admin.addNote(note1);
        Note note2 = new Note();
        note2.setNote("sdasdasdasdasdasdas ad sd sd sd sd 123 as dasdas ");
        note2.setName("note2");
        note2.setType(NoteType.REVIEWS);
        admin.addNote(note2);
        admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));
        userService.signup(admin);

        User client = new User();
        client.setUsername("client");
        client.setPassword("client");
        client.setEmail("client@email.com");
        client.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_CLIENT)));
        userService.signup(client);
    }

}
