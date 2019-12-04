package com.almis.awe.model.dto;

import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.model.entities.queries.Query;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class QueryData {
  private Query query;
  private String alias;
  private DatabaseConnection connection;
  private List<SortColumn> sortList;
  private Integer elementsPerPage;
  private Integer page;
  private ObjectNode parameters;
  private ComponentAddress launcherAddress;
}
