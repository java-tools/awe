package com.almis.awe.test;

import com.almis.awe.model.dto.CellData;

import java.util.HashMap;
import java.util.Map;

/**
 * User data test
 */
public class User {

  /**
   * User gender
   */
  // Enum gender
  public enum Gender {
    male, female
  }

  /**
   * User address
   */
  public static class Address {
    private String state;
    private String city;

    /**
     * @return the state
     */
    public String getState() {
      return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
      this.state = state;
    }

    /**
     * @return the city
     */
    public String getCity() {
      return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
      this.city = city;
    }
  }

  private int id;
  private String name;
  private Gender gender;
  private int age;
  private Address address;

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the gender
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * @param gender the gender to set
   */
  public void setGender(Gender gender) {
    this.gender = gender;
  }

  /**
   * @return the age
   */
  public int getAge() {
    return age;
  }

  /**
   * @param age the age to set
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * @return the address
   */
  public Address getAddress() {
    return address;
  }

  /**
   * @param address the address to set
   */
  public void setAddress(Address address) {
    this.address = address;
  }

  /**
   * Transform user in datalist row
   * 
   * @return User as datalist row
   */
  public Map<String, CellData> toDatalistRow() {
    Map<String, CellData> row = new HashMap<String, CellData>();
    row.put("id", new CellData(this.id));
    row.put("name", new CellData(this.name));
    row.put("gender", new CellData(this.getGender().toString().toLowerCase()));
    row.put("age", new CellData(this.age));
    row.put("state", new CellData(this.getAddress().getState()));
    row.put("city", new CellData(this.getAddress().getCity()));
    return row;
  }

}
