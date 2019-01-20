package pl.sda.testing.bookstore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookApi bookApi;
    @Mock
    private BookStore bookStore;

    private BookService bookService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookService(bookApi, bookStore);
    }

    @Test
    public void testGetAllBooksFromAuthorAndVerifySavingBooksToBookStore() {
        // given
        when(bookApi.fetchBooksByAuthor(anyString())).thenReturn(createBookList());
        List<Book> expected = createBookList();

        // when
        List<Book> actual = bookService.getBooksFromAuthor("Test");

        // then
        Assert.assertEquals(expected, actual);
        Mockito.verify(bookStore).saveBooks(eq(expected));
    }

    @Test(expected = ApiException.class)
    public void testBookApiIOExceptionInBookService() {
        // given
        when(bookApi.fetchBooksByAuthor(anyString())).thenThrow(new ApiException());

        // when
        List<Book> actual = bookService.getBooksFromAuthor("Test");
    }

    private List<Book> createBookList() {
        return Arrays.asList(
                new Book("Shining", "Stephen King", 1980),
                new Book("Lord of the Rings", "Tolkien", 1917),
                new Book("Harry Potter", "J.K. Rowling", 1997)
        );
    }
}