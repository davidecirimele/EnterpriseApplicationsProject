package com.enterpriseapplicationsproject.ecommerce;

import com.enterpriseapplicationsproject.ecommerce.data.entities.Address;
import com.enterpriseapplicationsproject.ecommerce.data.entities.User;
import com.enterpriseapplicationsproject.ecommerce.data.service.AddressService;
import com.enterpriseapplicationsproject.ecommerce.data.service.UserService;

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
import java.time.LocalDate;
import java.util.List;

@Component
public class DbGenerator implements ApplicationRunner {

  @Value("classpath:data/users.csv")
  private Resource usersRes;

  @Value("classpath:data/addresses.csv")
  private Resource addressesRes;

  @Value("classpath:data/products.csv")
  private Resource productsRes;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  protected UserService userService;

  @Autowired
  protected AddressService addressService;

  @Autowired
  //protected ProductsService productsService;

  //FIXME - BISOGNA INSERIRE LIBRI ADESSO

  public void createDb() {

    try {
      CSVParser usersCsv = CSVFormat.DEFAULT.withDelimiter(';')
          .parse(new InputStreamReader(usersRes.getInputStream()));
      for (CSVRecord record : usersCsv) {
          System.out.println(record.get(0));
          insertUser(record.get(0));
      }

      CSVParser productsCsv = CSVFormat.DEFAULT.withDelimiter(';')
              .parse(new InputStreamReader(productsRes.getInputStream()));
      for (CSVRecord record : productsCsv) {
        System.out.println(record.get(0));
        insertProduct(record.get(0));
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

  private void insertProduct( String record) {

    String[] array = record.split(",");

    System.out.println("RECORD : "+ record);

    String category = array[0];

    System.out.println("CATEGORY : "+ array[0]);

    Double weight = Double.parseDouble(array[1]);

    System.out.println("WEIGHT : "+ array[1]);

    LocalDate insertDate = LocalDate.parse(array[2]);

    System.out.println("DATE : "+ array[2]);

    Double price = Double.parseDouble(array[3]);

    System.out.println("PRICE : "+ array[3]);

    Integer stock = Integer.parseInt(array[4]);

    System.out.println("STOCK : "+ array[4]);

    //FIXME - BISOGNA INSERIRE LIBRI ADESSO
    /*ProductDto product = new ProductDto();
    product.setCategory(category);
    product.setWeight(weight);
    product.setPrice(price);
    product.setStock(stock);

    System.out.println("PRODUCT : "+ product);

    productsService.save(product);*/
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