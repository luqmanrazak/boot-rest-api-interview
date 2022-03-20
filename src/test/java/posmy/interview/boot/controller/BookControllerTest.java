package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import posmy.interview.boot.model.Book;
import posmy.interview.boot.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository mockRepository;

    private List<Book> books;

    @BeforeEach
    public void init() {
        books = Arrays.asList(
                new Book(1L, "Book 1", "Author 1", true),
                new Book(2L, "Book 2", "Author 2", true),
                new Book(3L, "Book 3", "Author 3", true)
        );
    }

    @WithMockUser("USER")
    @Test
    public void find_allBook_OK() throws Exception {

        when(mockRepository.findAll()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(books.size())))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Book 1")))
                .andExpect(jsonPath("$[0].author", is("Author 1")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Book 2")))
                .andExpect(jsonPath("$[1].author", is("Author 2")))
                .andExpect(jsonPath("$[1].available", is(true)))
        ;
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_bookId_OK() throws Exception {

        when(mockRepository.findById(1L)).thenReturn(Optional.of(books.get(0)));

        mockMvc.perform(get("/books/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Book 1")))
                .andExpect(jsonPath("$.author", is("Author 1")))
                .andExpect(jsonPath("$.available", is(true)));

        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    public void find_bookIdNotFound_404() throws Exception {
        mockMvc.perform(get("/books/999")).andExpect(status().isNotFound());
    }

    @Test
    public void update_book_OK() throws Exception {

        Book updateBook = new Book(3L, "Book.3", "Author.3", false);
        when(mockRepository.save(any(Book.class))).thenReturn(updateBook);

        mockMvc.perform(put("/books/3")
                        .content(om.writeValueAsString(updateBook))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Book.3")))
                .andExpect(jsonPath("$.author", is("Author.3")))
                .andExpect(jsonPath("$.available", is(false)));
    }

    @Test
    public void delete_book_OK() throws Exception {

        doNothing().when(mockRepository).deleteById(3L);

        mockMvc.perform(delete("/books/3"))
                .andExpect(status().isOk());

        verify(mockRepository, times(1)).deleteById(3L);
    }

}
