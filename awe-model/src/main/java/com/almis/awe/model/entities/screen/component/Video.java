/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Video Tag Class
 *
 * Used to parse video tag with XStream
 *
 *
 * Default HTML video tag
 *
 *
 * @author David FUENTES - 09/MAR/2016
 */
@XStreamAlias("video")
public class Video extends Component {

  private static final long serialVersionUID = 8799204817655283841L;

  // Tag loop attribute
  @XStreamAlias("loop")
  @XStreamAsAttribute
  private String loop = null;

  // Tag preload attribute
  @XStreamAlias("preload")
  @XStreamAsAttribute
  private String preload = null;

  // Tag autoplay attribute
  @XStreamAlias("autoplay")
  @XStreamAsAttribute
  private String autoplay = null;

  // Tag poster attribute
  @XStreamAlias("poster")
  @XStreamAsAttribute
  private String poster = null;

  // Tag poster attribute
  @XStreamAlias("controls")
  @XStreamAsAttribute
  private String controls = null;

  // Tag source attribute
  @XStreamAlias("src")
  @XStreamAsAttribute
  private String src = null;

  /**
   * Default constructor
   */
  public Video() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Video(Video other) throws AWException {
    super(other);
    this.loop = other.loop;
    this.preload = other.preload;
    this.autoplay = other.autoplay;
    this.poster = other.poster;
    this.controls = other.controls;
    this.src = other.src;
  }

  @Override
  public Video copy() throws AWException {
    return new Video(this);
  }

  /**
   * Returns controls value for the video
   *
   * @return String
   */
  public String getControls() {
    return controls != null && !controls.equals("") ? controls : "true";
  }

  /**
   * Stores the controls value for the video
   *
   * @param controls
   */
  public void setControls(String controls) {
    this.controls = controls;
  }

  /**
   * Returns the src attribute value
   *
   * @return String
   */
  public String getSrc() {
    return src;
  }

  /**
   * Stores the source attribute value
   *
   * @param src
   */
  public void setSrc(String src) {
    this.src = src;
  }

  /**
   * Returns the poster attribute value
   *
   * @return String
   */
  public String getPoster() {
    return poster;
  }

  /**
   * Stores the poster attribute value
   *
   * @param poster
   */
  public void setPoster(String poster) {
    this.poster = poster;
  }

  /**
   * Returns the autoplay attribute value
   *
   * @return String
   */
  public String getAutoplay() {
    return autoplay;
  }

  /**
   * Stores the autoplay attribute value
   *
   * @param autoplay
   */
  public void setAutoplay(String autoplay) {
    this.autoplay = autoplay;
  }

  /**
   * Returns the preload attribute value
   *
   * @return String
   */
  public String getPreload() {
    return preload;
  }

  /**
   * Stores the preload attribute value
   *
   * @param preload
   */
  public void setPreload(String preload) {
    this.preload = preload;
  }

  /**
   * Returns the tag loop attribute
   *
   * @return Tag Text
   */
  public String getLoop() {
    return loop;
  }

  /**
   * Stores the loop attribute value
   *
   * @param loop Tag loop
   */
  public void setLoop(String loop) {
    this.loop = loop;
  }

  /**
   * Retrieve component tag
   *
   * @return
   */
  @Override
  public String getComponentTag() {
    return "video";
  }
}
