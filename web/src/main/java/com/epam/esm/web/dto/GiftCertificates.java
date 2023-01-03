package com.epam.esm.web.dto;

import com.epam.esm.persistance.entity.GiftCertificate;
import java.util.List;
import lombok.Value;

/**
 * Data class created with purpose to encapsulate multiple {@link GiftCertificate} entries.
 *
 * @author Tamerlan Hurbanov
 * @see GiftCertificate
 * @since 1.0
 */
@Value
public class GiftCertificates {
    List<GiftCertificate> entries;
    long count;
}
