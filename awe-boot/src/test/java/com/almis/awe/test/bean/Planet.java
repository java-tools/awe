package com.almis.awe.test.bean;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Planet implements ResponseWrapper {

  private String name;
  private String rotationPeriod;
  private String orbitalPeriod;
  private String diameter;
  private String climate;
  private String gravity;
  private String terrain;
  private String surfaceWater;
  private String population;
  private List<String> residents;
  private List<String> films;
  private Date created;
  private Date edited;
  private String url;

  @Override
  public ServiceData toServiceData() throws AWException {
    DataList dataList = new DataListBuilder()
            .addColumn("name", Arrays.asList(new String[]{getName()}), "STRING")
            .addColumn("rotationPeriod", Arrays.asList(new String[]{getRotationPeriod()}), "STRING")
            .addColumn("orbitalPeriod", Arrays.asList(new String[]{getOrbitalPeriod()}), "STRING")
            .addColumn("diameter", Arrays.asList(new String[]{getDiameter()}), "STRING")
            .addColumn("climate", Arrays.asList(new String[]{getClimate()}), "STRING")
            .addColumn("gravity", Arrays.asList(new String[]{getGravity()}), "STRING")
            .addColumn("terrain", Arrays.asList(new String[]{getTerrain()}), "STRING")
            .addColumn("surfaceWater", Arrays.asList(new String[]{getSurfaceWater()}), "STRING")
            .addColumn("population", Arrays.asList(new String[]{getPopulation()}), "STRING")
            .addColumn("residents", getResidents(), "STRING")
            .addColumn("films", getFilms(), "STRING")
            .addColumn("created", Arrays.asList(new String[]{getName()}), "DATE")
            .addColumn("edited", Arrays.asList(new String[]{getName()}), "DATE")
            .addColumn("url", Arrays.asList(new String[]{getUrl()}), "STRING")
            .build();
    return new ServiceData().setDataList(dataList);
  }

  public String getName() {
    return name;
  }

  public Planet setName(String name) {
    this.name = name;
    return this;
  }


  @JsonProperty("rotation_period")
  public String getRotationPeriod() {
    return rotationPeriod;
  }

  public Planet setRotationPeriod(String rotationPeriod) {
    this.rotationPeriod = rotationPeriod;
    return this;
  }

  @JsonProperty("orbital_period")
  public String getOrbitalPeriod() {
    return orbitalPeriod;
  }

  public Planet setOrbitalPeriod(String orbitalPeriod) {
    this.orbitalPeriod = orbitalPeriod;
    return this;
  }

  public String getDiameter() {
    return diameter;
  }

  public Planet setDiameter(String diameter) {
    this.diameter = diameter;
    return this;
  }

  public String getClimate() {
    return climate;
  }

  public Planet setClimate(String climate) {
    this.climate = climate;
    return this;
  }

  public String getGravity() {
    return gravity;
  }

  public Planet setGravity(String gravity) {
    this.gravity = gravity;
    return this;
  }

  public String getTerrain() {
    return terrain;
  }

  public Planet setTerrain(String terrain) {
    this.terrain = terrain;
    return this;
  }

  @JsonProperty("surface_water")
  public String getSurfaceWater() {
    return surfaceWater;
  }

  public Planet setSurfaceWater(String surfaceWater) {
    this.surfaceWater = surfaceWater;
    return this;
  }

  public String getPopulation() {
    return population;
  }

  public Planet setPopulation(String population) {
    this.population = population;
    return this;
  }

  public List<String> getResidents() {
    return residents;
  }

  public Planet setResidents(List<String> residents) {
    this.residents = residents;
    return this;
  }

  public List<String> getFilms() {
    return films;
  }

  public Planet setFilms(List<String> films) {
    this.films = films;
    return this;
  }

  public Date getCreated() {
    return created;
  }

  public Planet setCreated(Date created) {
    this.created = created;
    return this;
  }

  public Date getEdited() {
    return edited;
  }

  public Planet setEdited(Date edited) {
    this.edited = edited;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Planet setUrl(String url) {
    this.url = url;
    return this;
  }
}
