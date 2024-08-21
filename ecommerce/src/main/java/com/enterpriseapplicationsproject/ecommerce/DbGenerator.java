package com.enterpriseapplicationsproject.ecommerce;

import com.enterpriseapplicationsproject.ecommerce.data.entities.*;
import com.enterpriseapplicationsproject.ecommerce.data.service.*;

import com.enterpriseapplicationsproject.ecommerce.dto.BookDto;
import com.enterpriseapplicationsproject.ecommerce.dto.GroupDto;
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
import java.util.ArrayList;
import java.util.List;

@Component
public class DbGenerator implements ApplicationRunner {

    @Value("classpath:data/users.csv")
    private Resource usersRes;

    @Value("classpath:data/addresses.csv")
    private Resource addressesRes;

    @Value("classpath:data/books.csv")
    private Resource productsRes;

    @Value("classpath:data/wishlist.csv")
    private Resource wishlistRes;

    @Value("classpath:data/wishlist_items.csv")
    private Resource wishlistItemsRes;

    @Value("classpath:data/groups.csv")
    private Resource groupsRes;

    @Value("classpath:data/group_memberships.csv")
    private Resource groupMembershipsRes;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    protected UserService userService;

    @Autowired
    protected AddressService addressService;

    @Autowired
    protected BooksService bookService;

    @Autowired
    protected GroupsService groupService;

    @Autowired
    protected WishlistsService wishlistService;

    @Autowired
    protected WishlistItemsService wishlistItemsService;

    public void createDb() {
        try {
            CSVParser usersCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(usersRes.getInputStream()));
            for (CSVRecord record : usersCsv) {
                //System.out.println(record.get(0));
                insertUser(record.get(0));
            }

            CSVParser booksCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(productsRes.getInputStream()));
            for (CSVRecord record : booksCsv) {
                //System.out.println(record.get(0));
                insertBook(record.get(0));
            }

            CSVParser addressesCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(addressesRes.getInputStream()));
            for (CSVRecord record : addressesCsv) {
                //System.out.println(record.get(0));

                insertAddress(record.get(0));

            }
            //GROUPS
            CSVParser groupsCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(groupsRes.getInputStream()));

            for (CSVRecord record : groupsCsv) {
                System.out.println(record.get(0));
                insertGroup(record.get(0));
            }

            //GROUP MEMBERSHIPS

            CSVParser groupMembershipsCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(groupMembershipsRes.getInputStream()));

            for (CSVRecord record : groupMembershipsCsv) {
                System.out.println(record.get(0));
                insertGroupMembership(record.get(0));
            }

            //WISH LIST

            CSVParser wishlistCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(wishlistRes.getInputStream()));

            for (CSVRecord record : wishlistCsv) {
                System.out.println(record.get(0));
                insertWishlist(record.get(0));

            }

            //WISH LIST ITEMS
            CSVParser wishlistItemsCsv = CSVFormat.DEFAULT.withDelimiter(';')
                    .parse(new InputStreamReader(wishlistItemsRes.getInputStream()));

            for (CSVRecord record : wishlistItemsCsv) {
                System.out.println(record.get(0));
                insertWishlistItem(record.get(0));
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

        List<UserDto> allUsers = userService.getAllDto();
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

    private void insertWishlist(String s) {
        String[] array = s.split(",");

        // Parse and retrieve relevant data from the CSV record
        int userIndex = Integer.parseInt(array[0]) - 1;  // Index for the user
        String name = array[1];
        int groupIndex = Integer.parseInt(array[2]) - 1; // Index for the group
        String privacy = array[3];

        // Retrieve the User and Group entities by their index in the corresponding lists
        List<UserDto> allUsersDto = userService.getAllDto();
        UserDto udto = allUsersDto.get(userIndex); // Retrieve all users as entities
        User user = userService.getUserById(udto.getId());

        List<GroupDto> allGroupsDto = groupService.getAllGroups();
        GroupDto gdto = allGroupsDto.get(groupIndex); // Retrieve all groups as entities
        Group group = groupService.getGroupById(gdto.getId());

        // Create a new Wishlist and set its properties
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(user);
        wishlist.setName(name);
        wishlist.setGroup(group);
        wishlist.setPrivacySetting(privacy);

        // Save the Wishlist entity
        wishlistService.save(wishlist);
    }

    private void insertWishlistItem(String s) {
        String[] array = s.split(",");

        int wishlistIndex = Integer.parseInt(array[1]) - 1;
        int bookIndex = Integer.parseInt(array[2]) - 1;
        int quantity = Integer.parseInt(array[3]);

        List<BookDto> allBooks = bookService.getBookDto();
        BookDto bookDto = allBooks.get(bookIndex);
        Book book = bookService.getBookById(bookDto.getId());

        List<Wishlist> allWishlists = wishlistService.getAll();
        Wishlist wishlist = allWishlists.get(wishlistIndex);

        WishlistItem wishlistItem = new WishlistItem();
        wishlistItem.setBook(book);
        wishlistItem.setWishlist(wishlist);
        wishlistItem.setQuantity(quantity);

        wishlistItemsService.save(wishlistItem);
    }


    private void insertGroup(String s) {
        String[] array = s.split(",");
        String name = array[1];

        Group group = new Group();
        group.setGroupName(name);

        groupService.save(group);
    }


    private void insertGroupMembership(String s) {
        String[] array = s.split(",");

        int groupIndex = Integer.parseInt(array[0]) - 1;
        int memberIndex = Integer.parseInt(array[1]) - 1;

        List<UserDto> allUsersDto = userService.getAllDto();
        UserDto udto = allUsersDto.get(memberIndex);
        User user = userService.getUserById(udto.getId());

        List<GroupDto> allGroupsDto = groupService.getAllGroups();
        GroupDto gdto = allGroupsDto.get(groupIndex);
        Group group = groupService.getGroupById(gdto.getId());

        group.getMembers().add(user);
        groupService.save(group);
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        createDb();
    }
}
