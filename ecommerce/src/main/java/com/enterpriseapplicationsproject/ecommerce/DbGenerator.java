package com.enterpriseapplicationsproject.ecommerce;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.data.service.BooksService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;

import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.SaveBookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.UserDto;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class DbGenerator implements ApplicationRunner {

    @Value("classpath:data/users.csv")
    private Resource usersRes;

    @Value("classpath:data/addresses.csv")
    private Resource addressesRes;

    @Value("classpath:data/books.csv")
    private Resource productsRes;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    protected UserService userService;

    @Autowired
    protected AddressService addressService;

    @Autowired
    protected BooksService bookService;

    public void createDb() {
        try {
            CSVParser usersCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(usersRes.getInputStream()));
            for (CSVRecord record : usersCsv) {
                System.out.println(record.get(0));
                insertUser(record.get(0));
            }

            CSVParser booksCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(productsRes.getInputStream()));
            for (CSVRecord record : booksCsv) {
                System.out.println(record.get(0));
                insertBook(record.get(0));
            }

            CSVParser addressesCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(addressesRes.getInputStream()));
            for (CSVRecord record : addressesCsv) {
                System.out.println(record.get(0));

                insertAddress(record.get(0));

            }
        } catch (IOException e) {
            throw new RuntimeException("ERROR TO GENERATE DB TEST");
        }
    }

    private void insertAddress( String record) {

        String[] array = record.split(",");

        int counter = Integer.parseInt(array[0])-1;
        String street = array[1];
        String province = array[2];
        String city = array[3];
        String state = array[4];
        String zipCode = array[5];
        String additionalInfo = array[6];

        List<UserDto> allUsers = userService.getAll();
        UserDto userDto = allUsers.get(counter);

        User user = userService.getUserById(userDto.getId());

        Address address = new Address();
        address.setUser(user);
        address.setStreet(street);
        address.setProvince(province);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(zipCode);
        address.setAdditionalInfo(additionalInfo);
        address.setIsValidAddress(true);
        address.setDefaultAddress(true);

        //user.setAddress(new Address(streetAddress, numberAddress, cityAddress));

        addressService.save(address);
    }

    private void insertBook( String record) {

        String[] array = record.split(",");

        String category = array[0];

        Double weight = Double.parseDouble(array[1]);

        Double price = Double.parseDouble(array[3]);

        Integer stock = Integer.parseInt(array[4]);

        String title = array[5];

        String author = array[6];

        String ISBN = array[7];

        int pages = Integer.parseInt(array[8]);

        String edition = array[9];

        String format = array[10];

        String genre = array[11];

        String language = array[12];

        String publisher = array[13];

        int age = Integer.parseInt(array[14]);

        Date publishDate = Date.valueOf(array[15]);

        System.out.println("RECORD : "+ record);

        SaveBookDto book = new SaveBookDto();

        book.setTitle(title);
        book.setCategory(category);
        book.setAge(age);
        book.setFormat(format);
        book.setAuthor(author);
        book.setEdition(edition);
        book.setISBN(ISBN);
        book.setPrice(price);
        book.setPages(pages);
        book.setGenre(genre);
        book.setStock(stock);
        book.setWeight(weight);
        book.setLanguage(language);
        book.setPublisher(publisher);
        book.setPublishDate(publishDate);

        bookService.save(book);
    }

    private void insertUser( String record) {

        String[] array = record.split(",");

        String lastName = array[0];
        String firstName = array[1];
        LocalDate birthDate = LocalDate.parse(array[2]);
        String email = array[3];
        String password = array[4];
        String phoneNumber = array[5];

        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);

        user.setBirthDate(birthDate);
        String encodedPassword = passwordEncoder.encode(password);
        user.setCredentials(email,encodedPassword);
        user.setPhoneNumber(phoneNumber);

        //user.setAddress(new Address(streetAddress, numberAddress, cityAddress));

        userService.save(user);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createDb();
    }
}
