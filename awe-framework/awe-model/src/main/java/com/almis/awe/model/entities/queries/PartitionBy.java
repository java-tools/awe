package com.almis.awe.model.entities.queries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * PartitionBy Class
 * Used to parse the files Queries.xml with XStream
 * Generates a group by statement inside a query
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("partition-by")
public class PartitionBy extends GroupBy {
}
