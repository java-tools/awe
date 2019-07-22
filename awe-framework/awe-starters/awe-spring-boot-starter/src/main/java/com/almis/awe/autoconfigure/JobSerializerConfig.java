package com.almis.awe.autoconfigure;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Collection;

/**
 * Initialize serializer beans
 *
 * @author pgarcia
 */
@Configuration
public class JobSerializerConfig {

  /**
   * Serializer initialization
   * @param marshaller Marshaller
   */
  public JobSerializerConfig(XStreamMarshaller marshaller) {
    XStream xstream = marshaller.getXStream();

    // clear out existing permissions and set own ones
    xstream.addPermission(NoTypePermission.NONE);

    // allow some basics
    xstream.addPermission(NullPermission.NULL);
    xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
    xstream.allowTypeHierarchy(Collection.class);

    // allow any type from the same package
    xstream.allowTypesByWildcard(new String[] {
      "java.*", "com.almis.awe.model.entities.**"
    });
  }
}
