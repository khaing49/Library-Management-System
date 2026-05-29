package com.app.library.service;
import com.app.library.model.Book;
import com.app.library.model.Member;
import com.app.library.model.BorrowingRecord;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
@Service
public class LibraryService {
    // In-memory storage using ArrayList
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();
    // ==================== Book Methods ====================
    // Get all books
    public List<Book> getAllBooks() {
        return books;
    }
    // Get a book by ID
    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }
    // Get book by genre
    public List<Book> getBooksByGenre(String genre) {
        List<Book> allBooks = this.books;
        return allBooks.stream()
                .filter(book -> (book.getGenre()).toLowerCase().contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }
    //Get book by author,filtered by genre
    public List<Book> getBooksByAuthorAndGenre(String author, String genre) {
        List<Book> allBooks = this.books;
        return allBooks.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author)) // Filter by author
                .filter(book -> genre == null || book.getGenre().toLowerCase().contains(genre.toLowerCase())) // Optional filter by genre
                .collect(Collectors.toList());
    }
    //Get book by due date
    public List<Book> getBooksDueOnDate(LocalDate dueDate) {
        List<BorrowingRecord> allRecords = this.borrowingRecords;
        List<BorrowingRecord> dueRecords = allRecords.stream()
                .filter(record -> record.getDueDate() != null && record.getDueDate().equals(dueDate))
                .toList();
        List<Book> dueBooks = new ArrayList<>();
        for (BorrowingRecord record : dueRecords) {
            Book book = record.getBook();
            if (book != null) {
                dueBooks.add(book);
            }
        }
        return dueBooks;
    }
    //Add book
    public void addBook(Book book) {
        books.add(book);
    }
    // Update a book
    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                break;
            }
        }
    }
    // Delete a book by ID
    public void deleteBook(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }
    // ==================== Member Methods ====================
    // Get all members
    public List<Member> getAllMembers() {
        return members;
    }
    // Get a member by ID
    public Optional<Member> getMemberById(Long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }
    // Add a new member
    public void addMember(Member member) {
        members.add(member);
    }
    // Update a member
    public void updateMember(Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getId().equals(updatedMember.getId())) {
                members.set(i, updatedMember);
                break;
            }
        }
    }
    // Delete a member by ID
    public void deleteMember(Long id) {
        members.removeIf(member -> member.getId().equals(id));
    }
    // ==================== BorrowingRecord Methods ====================
    // Get all borrowing records
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecords;
    }
    // Borrow a book (create a new borrowing record)
    public void borrowBook(BorrowingRecord record) {
        // Set borrow date and due date (e.g., due date = borrow date + 14 days)
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        borrowingRecords.add(record);
        // Decrease the available copies of the book
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() - 1);
    }
    // Return a book (update the borrowing record with the return date)
    public void returnBook(Long recordId, LocalDate returnDate) {
        for (BorrowingRecord record : borrowingRecords) {
            if (record.getId().equals(recordId)) {
                record.setReturnDate(returnDate);
                // Increase the available copies of the book
                Book book = record.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                break;
            }
        }
    }
}

