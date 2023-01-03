package com.epam.esm.web.dto;

import com.epam.esm.persistance.entity.Tag;
import java.util.List;
import lombok.Value;

/**
 * Data class created with purpose to encapsulate multiple {@link Tag} entries.
 *
 * @author Tamerlan Hurbanov
 * @see Tag
 * @since 1.0
 */
@Value
public class Tags {
  List<Tag> entries;
  long count;
}
