//package ru.yandex.practicum.filmorate.storage;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.assertj.core.api.AssertionsForClassTypes;
//import org.assertj.core.api.Assertions;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.model.Mpa;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.DAO.FilmDbStorage;
//import ru.yandex.practicum.filmorate.storage.DAO.UserDbStorage;
//
//import java.time.LocalDate;
//
//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class FilmStorageTest {
//
//    private final FilmDbStorage inDbFilmStorage;
//
//    private final UserDbStorage inDbUserStorage;
//
//    private final LikeDbStorage inDbLikeStorage;
//
//    private Film film = Film.builder()
//            .name("testFilm")
//            .description("desc")
//            .releaseDate(LocalDate.of(2020, 1, 1))
//            .duration(110)
//            .mpa(new Mpa(1, "G"))
//            .genres(null)
//            .build();
//
//    @Test
//    void addFilmTest() {
//        inDbFilmStorage.add(film);
//        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
//        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
//    }
//
//    @Test
//    void updateFilmTest() {
//        inDbFilmStorage.add(film);
//        film.setName("testUpdateFilm");
//        film.setDescription("testUpdateDesc");
//        inDbFilmStorage.update(film);
//        AssertionsForClassTypes.assertThat(inDbFilmStorage.getById(film.getId()))
//                .hasFieldOrPropertyWithValue("name", "testUpdateFilm")
//                .hasFieldOrPropertyWithValue("description", "testUpdateDesc");
//    }
//
//    @Test
//    void getFilmTest() {
//        inDbFilmStorage.add(film);
//        inDbFilmStorage.getById(film.getId());
//        AssertionsForClassTypes.assertThat(inDbFilmStorage.getById(film.getId())).hasFieldOrPropertyWithValue("id", film.getId());
//    }
//
//    @Test
//    void updateFilmNotFoundTest() {
//        Film filmForUpdate = Film.builder()
//                .id(9999)
//                .name("testFilm")
//                .description(("desc"))
//                .releaseDate(LocalDate.of(2020, 1, 1))
//                .mpa(new Mpa(1, "G"))
//                .genres(null)
//                .build();
//        Assertions.assertThatThrownBy(() -> inDbFilmStorage.update(filmForUpdate))
//                .isInstanceOf(ObjectNotFoundException.class);
//    }
//
//    @Test
//    void addLikeFilmTest() {
//        User user = User.builder()
//                .id(1)
//                .email("example@mail.mail")
//                .login("login")
//                .name("Doe")
//                .birthday(LocalDate.of(2000, 12, 22))
//                .build();
//
//        Film filmForLike = Film.builder()
//                .name("testFilm")
//                .description("desc")
//                .releaseDate(LocalDate.of(2020, 1, 1))
//                .duration(110)
//                .mpa(new Mpa(1, "G"))
//                .genres(null)
//                .build();
//
//        inDbUserStorage.add(user);
//        inDbFilmStorage.add(filmForLike);
//        System.out.println(user.getId() + " - Это юзер Id!");
//        System.out.println(filmForLike.getId() + " - Это фильм Id!");
//        inDbLikeStorage.addLike(filmForLike.getId(), user.getId());
//        assertThat(inDbFilmStorage.getBestFilms(filmForLike.getId()).isEmpty());
//        assertThat(inDbFilmStorage.getBestFilms(filmForLike.getId())).isNotNull();
//        Assertions.assertThat(inDbFilmStorage.getBestFilms(filmForLike.getId()).size() == 2);
//    }
//
//    @Test
//    void removeFilmLikeTest() {
//        User user1 = User.builder()
//                .id(1)
//                .email("example@mail.mail")
//                .login("login")
//
//                .name("Doe")
//                .birthday(LocalDate.of(2000, 12, 22))
//                .build();
//
//        Film filmForLike = Film.builder()
//                .name("testFilm")
//                .description("desc")
//                .releaseDate(LocalDate.of(2020, 1, 1))
//                .duration(110)
//                .mpa(new Mpa(1, "G"))
//                .genres(null)
//                .build();
//
//        inDbUserStorage.add(user1);
//        inDbFilmStorage.add(filmForLike);
//        inDbFilmStorage.add(filmForLike);
//        inDbLikeStorage.addLike(filmForLike.getId(), user1.getId());
//        inDbLikeStorage.removeLike(filmForLike.getId(), user1.getId());
//        assertThat(inDbFilmStorage.getBestFilms(filmForLike.getId()).isEmpty());
//        assertThat(inDbFilmStorage.getBestFilms(filmForLike.getId())).isNotNull();
//        Assertions.assertThat(inDbFilmStorage.getBestFilms(filmForLike.getId()).size() == 1);
//    }
//
//    @Test
//    void getBestFilmTest() {
//
//        Film filmForLike = Film.builder()
//                .name("testFilm")
//                .description("desc")
//                .releaseDate(LocalDate.of(2020, 1, 1))
//                .duration(110)
//                .mpa(new Mpa(1, "G"))
//                .genres(null)
//                .build();
//
//        Film otherFilmForLike = Film.builder()
//                .name("testFilm")
//                .description("desc")
//                .releaseDate(LocalDate.of(2020, 1, 1))
//                .duration(110)
//                .mpa(new Mpa(1, "G"))
//                .genres(null)
//                .build();
//
//        inDbFilmStorage.add(film);
//        inDbFilmStorage.add(filmForLike);
//        inDbFilmStorage.add(otherFilmForLike);
//
//        User user = User.builder()
//                .id(1)
//                .email("example@mail.mail")
//                .login("login")
//                .name("Doe")
//                .birthday(LocalDate.of(2000, 12, 22))
//                .build();
//
//        User user1 = User.builder()
//                .id(1)
//                .email("example@mail.mail")
//                .login("login")
//                .name("Doe")
//                .birthday(LocalDate.of(2000, 12, 22))
//                .build();
//
//        User user2 = User.builder()
//                .id(1)
//                .email("example@mail.mail")
//                .login("login")
//                .name("Doe")
//                .birthday(LocalDate.of(2000, 12, 22))
//                .build();
//
//        inDbUserStorage.add(user);
//        inDbUserStorage.add(user1);
//        inDbUserStorage.add(user2);
//
//        inDbLikeStorage.addLike(film.getId(), user.getId());
//        inDbLikeStorage.addLike(filmForLike.getId(), user1.getId());
//        inDbLikeStorage.addLike(otherFilmForLike.getId(), user2.getId());
//        inDbLikeStorage.addLike(film.getId(), user1.getId());
//        inDbLikeStorage.addLike(film.getId(), user2.getId());
//        assertThat(inDbFilmStorage.getBestFilms(film.getId())).isNotNull();
//        Assertions.assertThat(inDbFilmStorage.getBestFilms(film.getId()).size() == 6);
//    }
//}
